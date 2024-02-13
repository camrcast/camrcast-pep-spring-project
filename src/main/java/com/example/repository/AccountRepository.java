package com.example.repository;
import org.springframework.data.jpa.repository.*;
import com.example.entity.*;

public interface AccountRepository extends JpaRepository<Account, Long>{
}
