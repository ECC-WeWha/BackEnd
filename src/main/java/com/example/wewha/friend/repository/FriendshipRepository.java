package com.example.wewha.friend.repository;

import com.example.wewha.common.entity.User;
import com.example.wewha.friend.FriendshipStatus;
import com.example.wewha.friend.entity.UserFriendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<UserFriendship, Long> {
    boolean existsByRequesterAndReceiverAndStatus(User currentUser, User user, FriendshipStatus friendshipStatus);
}
