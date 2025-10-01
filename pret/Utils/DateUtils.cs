
using System;

namespace Pret.Utils
{
    public static class DateUtils
{
    /// <summary>
    /// Convertion d'un DateOnly en DateTime à minuit (00:00) UTC.
    /// </summary>
    public static DateTime ToDateTimeUtc(DateOnly date)
    {
        return date.ToDateTime(TimeOnly.MinValue, DateTimeKind.Utc);
    }

    /// <summary>
    /// Convertion d'un DateTime en DateOnly (ignore l'heure).
    /// </summary>
    public static DateOnly ToDateOnly(DateTime dateTime)
    {
        return DateOnly.FromDateTime(dateTime);
    }

    /// <summary>
    /// Retourne DateTime UTC correspondant à aujourd'hui.
    /// </summary>
    public static DateTime NowUtc()
    {
        return DateTime.UtcNow;
    }

    /// <summary>
    /// Retourne DateOnly correspondant à aujourd'hui.
    /// </summary>
    public static DateOnly Today()
    {
        return DateOnly.FromDateTime(DateTime.UtcNow);
    }
}

}
