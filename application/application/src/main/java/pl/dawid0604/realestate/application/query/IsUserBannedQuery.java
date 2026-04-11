/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query;

import pl.dawid0604.realestate.application.validation.ValidEmail;

public record IsUserBannedQuery(@ValidEmail String email) implements Query {}
