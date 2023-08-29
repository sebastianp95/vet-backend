package com.dev.vetbackend.repository;

import com.dev.vetbackend.entity.Exam;
import com.dev.vetbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {
List<Exam> findAllByUser(User user);
List<Exam> findAllByUserAndPetId(User user, Long id);

}
