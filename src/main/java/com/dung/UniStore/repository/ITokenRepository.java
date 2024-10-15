package com.dung.UniStore.repository;


import com.dung.UniStore.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITokenRepository  extends JpaRepository<Token, String> {
}
