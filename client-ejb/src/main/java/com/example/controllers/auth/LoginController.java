    package com.example.controllers.auth;

    import com.example.controllers.utils.Flash;
    import com.example.controllers.utils.UserSession;
    import com.example.models.ClientCourant;
    import com.example.remotes.ChangeServiceRemote;
    import com.example.remotes.ClientCourantServiceRemote;
    import com.example.remotes.ClientCourantStatefulServiceRemote;
    import com.example.utils.Url;

    import jakarta.annotation.PostConstruct;
    import jakarta.ejb.EJB;
    import jakarta.inject.Inject;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.annotation.WebServlet;
    import jakarta.servlet.http.HttpServlet;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import jakarta.servlet.http.HttpSession;

    import java.io.IOException;
    import java.util.Hashtable;

    import javax.naming.Context;
    import javax.naming.InitialContext;

    @WebServlet("/login")
    public class LoginController extends HttpServlet {

        @EJB(lookup = "java:global/server-ejb/ClientCourantService!com.example.remotes.ClientCourantServiceRemote")
        private ClientCourantServiceRemote clientCourantServiceRemote;

        // @EJB(lookup =
        // "java:global/server-ejb/ChangeService!com.example.remotes.ChangeServiceRemote")
        // @EJB(lookup =
        // "java:global/change-ejb/ChangeService!com.example.remotes.ChangeServiceRemote")
        
        @EJB(lookup = "java:global/server-ejb/ClientCourantStatefulService!com.example.remotes.ClientCourantStatefulServiceRemote")
        private ClientCourantStatefulServiceRemote clientCourantStatefulServiceRemote;
        
        private ChangeServiceRemote changeServiceRemote;
        @PostConstruct
        private void initRemoteEJB() {
            try {
                Hashtable<String, Object> jndiProps = new Hashtable<>();
                jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
                jndiProps.put(Context.PROVIDER_URL, "remote+http://localhost:9090");

                // Ajout de l'authentification
                jndiProps.put(Context.SECURITY_PRINCIPAL, "ejbuser");
                jndiProps.put(Context.SECURITY_CREDENTIALS, "ejbpass");

                Context ctx = new InitialContext(jndiProps);

                changeServiceRemote = (ChangeServiceRemote) ctx
                        .lookup("change-ejb/ChangeService!com.example.remotes.ChangeServiceRemote");

                System.out.println("ChangeServiceRemote initialisé avec succès !");
            } catch (Exception e) {
                System.err.println("Erreur lors de l'initialisation de ChangeServiceRemote:");
                e.printStackTrace();
            }
        }

        @Override
        protected void doGet(
                HttpServletRequest request,
                HttpServletResponse response)
                throws ServletException, IOException {

            Flash.loadFlashMessage(request);
            String page = Url.pages + "/login.jsp";
            System.out.println("taille>>>> " + changeServiceRemote.getAllDevises().size());
            String landingPage = page;
            request.getRequestDispatcher(landingPage).forward(request, response);
            // request.getRequestDispatcher("/login.jsp").forward(request, response);
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            String email = request.getParameter("email");
            String mdp = request.getParameter("mdp");
            System.out.println("hellooooo post <<<<<");
            try {

                ClientCourant clientCourant = clientCourantStatefulServiceRemote.login(email, mdp);
                // clientCourant.getDirection().getLibelle();
                request.setAttribute("content", Url.pages + "/home.jsp");
                request.setAttribute("fonctionality", "Home");
                request.setAttribute("title", "Prolongement-admin");
                // HttpSession session = request.getSession();
                // session.setAttribute("client", clientCourant);
                UserSession.setSessionRemote(request, clientCourantStatefulServiceRemote);

            } catch (Exception e) {

                Flash.set(request, "message", "Erreur: " + e.getMessage());
                Flash.set(request, "message_type", "danger");
                response.sendRedirect(request.getContextPath() + "/");

            }
            response.sendRedirect(request.getContextPath() + "/home");
        }

        // @Override
        // protected void doGet(HttpServletRequest request, HttpServletResponse
        // response)
        // throws ServletException, IOException {
        // String page = Url.pages + "/home.jsp";
        // // double montant = Double.parseDouble(request.getParameter("montant"));
        // // String source = request.getParameter("source");
        // // String cible = request.getParameter("cible");

        // // // double resultat = changeService.convertir(montant, source, cible);
        // request.setAttribute("content", page);
        // request.setAttribute("fonctionality", "Home");
        // request.setAttribute("title", "Prolongement-admin");
        // // request.setAttribute("source", source);
        // // request.setAttribute("cible", cible);

        // String landingPage = Url.layout + "/layout.jsp";
        // request.getRequestDispatcher(landingPage).forward(request, response);
        // // request.getRequestDispatcher("/login.jsp").forward(request, response);
        // }
    }