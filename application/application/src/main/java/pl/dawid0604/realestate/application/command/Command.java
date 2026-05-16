/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

public sealed interface Command
        permits ActivateAdvertisementCommand, ActivateUserCommand, AddAdvertisementPhotoCommand, BanUserCommand, CreateAdvertisementCommand, DeactivateAdvertisementCommand, DeleteAdvertisementCommand, DeleteAdvertisementPhotoCommand, DeleteUserCommand, LoginUserCommand, RefreshTokenCommand, RegisterUserCommand, SetAsFeaturedAdvertisementCommand, SetAsSoldAdvertisementCommand, UnbanUserCommand, UpdateAdvertisementCommand, UpdateUserProfileCommand, UpdateUserPasswordCommand, UserLogoutCommand {}
