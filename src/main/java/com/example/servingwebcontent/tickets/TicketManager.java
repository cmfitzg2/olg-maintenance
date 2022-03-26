package com.example.servingwebcontent.tickets;

import com.example.servingwebcontent.users.OLGUser;

import java.util.ArrayList;
import java.util.List;

public class TicketManager {
    public static List<Ticket> tickets = new ArrayList<>();
    public static final String STATUS_TO_DO = "To Do";
    public static final String STATUS_IN_PROGRESS = "In Progress";
    public static final String STATUS_VERIFYING = "Verifying";
    public static final String STATUS_DONE = "Done";
    public static final String PRIORITY_LOW = "Low";
    public static final String PRIORITY_MEDIUM = "Medium";
    public static final String PRIORITY_HIGH = "High";
    public static int TO_DO_COUNT = 0;
    public static int IN_PROGRESS_COUNT = 0;
    public static int VERIFYING_COUNT = 0;
    public static int DONE_COUNT = 0;
    public static int currentTicketNumber = 0;

    public static List<Ticket> getTicketsByUser(OLGUser user) {
        List<Ticket> ticketList = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getAssignedTo().equals(user.getUid())) {
                ticketList.add(ticket);
            }
        }
        return ticketList;
    }

    public static List<Ticket> getTicketsByStatus(String status) {
        List<Ticket> ticketList = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getStatus().equals(status)) {
                ticketList.add(ticket);
            }
        }
        return ticketList;
    }

    public static Ticket getTicketByUid(String uid) {
        for (Ticket ticket : tickets) {
            if (ticket.getUid().equals(uid)) {
                return ticket;
            }
        }
        System.out.println("DIDN'T FIND TICKET BY UID " + uid);
        return null;
    }

    public static void setTicketByUid(String uid, Ticket ticket) {
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getUid().equals(uid)) {
                tickets.set(i, ticket);
                return;
            }
        }
        System.out.println("DIDN'T FIND TICKET BY UID " + uid);
    }

    public static void updateTicketCounts() {
        for (Ticket ticket : tickets) {
            switch (ticket.getStatus()) {
                case STATUS_TO_DO:
                    TO_DO_COUNT++;
                    break;
                case STATUS_IN_PROGRESS:
                    IN_PROGRESS_COUNT++;
                    break;
                case STATUS_VERIFYING:
                    VERIFYING_COUNT++;
                    break;
                case STATUS_DONE:
                    DONE_COUNT++;
                    break;
            }
        }
    }

}
