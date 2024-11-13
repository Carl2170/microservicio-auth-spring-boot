package org.example.msvcauth.domain.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.msvcauth.domain.User;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersPage {
    private List<User> users;
    private int totalCount;
    private int totalPages;
}
