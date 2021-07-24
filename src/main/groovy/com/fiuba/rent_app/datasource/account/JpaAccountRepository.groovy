package com.fiuba.rent_app.datasource.account

import com.fiuba.rent_app.domain.account.Account
import org.springframework.data.repository.CrudRepository

interface JpaAccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findById(Long accountId)
}
