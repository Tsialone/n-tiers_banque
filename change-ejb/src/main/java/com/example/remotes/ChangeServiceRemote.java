package com.example.remotes;

import jakarta.ejb.Remote;

@Remote
public interface ChangeServiceRemote {
    String hello(String name);
}
