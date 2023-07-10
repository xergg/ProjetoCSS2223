package pt.ul.fc.css.example.demo.utils;

/**
 * Represents an enum for a Poll and Project status. APPROVED if it ended and it had more positive
 * than negative votes, NEGATIVE if it closed and had more negative than positive votes, and ONGOING
 * if it's still ongoing.
 */
public enum StatusPoll {
  APPROVED,
  DENIED,
  ONGOING
}
