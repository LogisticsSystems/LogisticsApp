package com.company.logistics.dto;

import com.company.logistics.enums.PackageStatus;

import java.time.LocalDateTime;

public record PackageSnapshot(
        int id,
        PackageStatus status,
        LocalDateTime eta,
        String message
) { }
