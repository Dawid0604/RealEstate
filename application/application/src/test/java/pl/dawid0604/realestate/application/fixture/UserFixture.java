/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.fixture;

import java.time.Instant;

import lombok.experimental.UtilityClass;
import pl.dawid0604.realestate.domain.ContactDetails;
import pl.dawid0604.realestate.domain.Email;
import pl.dawid0604.realestate.domain.FullName;
import pl.dawid0604.realestate.domain.Identifier;
import pl.dawid0604.realestate.domain.Password;
import pl.dawid0604.realestate.domain.PhoneNumber;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.UserRole;
import pl.dawid0604.realestate.domain.UserStatus;

@UtilityClass
public class UserFixture {

    public static String getDummyEmail() {
        return "xyz@mail.com";
    }

    public static User.Builder getDummyUserBuilder() {
        return User.reconstitute()
                .id(Identifier.generate())
                .createdAt(Instant.now())
                .email(new Email(getDummyEmail()))
                .password(Password.ofHashed("$xyz"))
                .fullName(new FullName("John", "Doe"))
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER_ROLE)
                .contactDetails(
                        new ContactDetails(
                                new Email(getDummyEmail()), new PhoneNumber("+48123456789")));
    }
}
