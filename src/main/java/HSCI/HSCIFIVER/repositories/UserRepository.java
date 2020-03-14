package HSCI.HSCIFIVER.repositories;

import HSCI.HSCIFIVER.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    public User findUserByUsername(String username);
}
