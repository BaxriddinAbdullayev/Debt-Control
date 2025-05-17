package uz.alifservice.repository.auth;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.alifservice.domain.auth.User;
import uz.alifservice.enums.GeneralStatus;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id = :id")
    Optional<User> findByIdWithRoles(@Param(value = "id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM auth_users_roles WHERE user_id = :userId", nativeQuery = true)
    void deleteRolesByUserId(@Param(value = "userId") Long userId);

    Optional<User> findByIdAndDeletedFalse(Long id);

    Optional<User > findByUsernameAndActiveTrueAndDeletedFalse(String username);

    Optional<User > findByIdAndActiveTrue(Integer id);

    @Transactional
    @Modifying
    @Query("update User set status=?2 where id = ?1")
    void changeStatus(Long id, GeneralStatus status);

    @Transactional
    @Modifying
    @Query("update User set password=?2 where id = ?1")
    void updatePassword(Long id, String password);


    @Transactional
    @Modifying
    @Query("update User set tempUsername=?2 where id = ?1")
    void updateTempUsername(Integer id, String tempUsername);

    @Transactional
    @Modifying
    @Query("update User set username=?2 where id = ?1")
    void updateUsername(Integer id, String username);
}