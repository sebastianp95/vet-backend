package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.Exam;
import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.repository.ExamRepository;
import com.dev.vetbackend.repository.PetRepository;
import com.dev.vetbackend.security.UserDetailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExamServiceImpl implements ExamService{

    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;

    @Override
    public Exam save(Exam exam, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found for ID: " + petId));
        User user = userDetailServiceImpl.getAuthenticatedUser();

        exam.setPet(pet);
        exam.setUser(user);

        return examRepository.save(exam);
    }

    @Override
    public List<Exam> findPetExams(Long petId) {
        User user = userDetailServiceImpl.getAuthenticatedUser();
        return examRepository.findAllByUserAndPetId(user, petId);
    }

    @Override
    public Exam findById(Long id) {
        Optional<Exam> examOptional = examRepository.findById(Math.toIntExact(id));
        return examOptional.orElseThrow(() -> new RuntimeException("Exam not found for ID: " + id));
    }

    @Override
    public Exam update(Long id, Exam exam) {
        Exam existingExam = findById(id);
        existingExam.setDate(exam.getDate());
        existingExam.setType(exam.getType());
        existingExam.setFileKey(exam.getFileKey());
        existingExam.setExtension(exam.getExtension());
        return examRepository.save(existingExam);
    }

    @Override
    public void delete(Long id) {
        Exam exam = findById(id);
        examRepository.delete(exam);
    }
}
