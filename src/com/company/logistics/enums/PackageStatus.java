package com.company.logistics.enums;

public enum PackageStatus {
    UNASSIGNED,
    PENDING,
    IN_TRANSIT,
    DELIVERED;

    public String toString() {
        String print = "";

        switch (this) {
            case UNASSIGNED -> print = "Awaiting route assignment";
            case PENDING -> print = "Awaiting truck assignment";
            case IN_TRANSIT -> print = "In transit";
            case DELIVERED -> print = "Delivered";
        }
        return print;
    }
}
