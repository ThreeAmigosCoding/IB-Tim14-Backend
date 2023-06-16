package com.example.demo.repository.user;

import com.example.demo.model.user.User;
import com.example.demo.model.user.UserRecentPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRecentPasswordRepository extends JpaRepository<UserRecentPassword, Integer> {

    List<UserRecentPassword> findByUserOrderByReplaceDateDesc(User user);

}
