package com.reddit.redditlight.Repositories;

import com.reddit.redditlight.Models.ApplicationUser;
import com.reddit.redditlight.Models.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
  List<Message> findAllByReceivingUser(ApplicationUser receivingUser);

  List<Message> findAllBySendingUser(ApplicationUser sendingUser);

//  @Query("from Message m where m.userRead=false AND m.receivingUser=user")
  int countAllByUserReadIsFalseAndReceivingUserIs(ApplicationUser user);
}
