/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command;

public sealed interface Command
        permits ActivateAdvertisementCommand,
                ActivateUserCommand,
                AddAdvertisementPhotoCommand,
                BanUserCommand,
                CreateAdvertisementCommand,
                DeactivateAdvertisementCommand,
                DeleteAdvertisementCommand,
                DeleteUserCommand,
                DisableFeaturedStateAdvertisementCommand,
                HandleUserLoginCommand,
                RegisterUserCommand,
                RemoveAdvertisementPhotoCommand,
                SetAsFeaturedAdvertisementCommand,
                SetAsSoldAdvertisementCommand,
                UnbanUserCommand,
                UpdateAdvertisementDescriptionCommand,
                UpdateAdvertisementLocalityCommand,
                UpdateAdvertisementPriceCommand,
                UpdateAdvertisementTitleCommand,
                UpdateUserAvatarCommand,
                UpdateUserContactDetailsCommand,
                UpdateUserEmailCommand,
                UpdateUserFullNameCommand,
                UpdateUserPasswordCommand {}
