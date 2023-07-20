package com.dev.vetbackend.services;

import com.dev.vetbackend.constants.ProductType;
import com.dev.vetbackend.constants.SubscriptionPlan;
import com.dev.vetbackend.entity.Product;
import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.entity.Vaccination;

import com.dev.vetbackend.exception.CustomException;
import com.dev.vetbackend.repository.ProductRepository;
import com.dev.vetbackend.repository.VaccinationRepository;
import com.dev.vetbackend.security.UserDetailServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final VaccinationRepository vaccinationRepository;
    private final UserDetailServiceImpl userDetailServiceImpl;
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Product> findAllByUserAndProductType(Pageable pageable, Long id, String name, String manufacturer, String targetSpecies, ProductType productType, String vaccinationType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> productRoot = cq.from(Product.class);

        List<Predicate> predicates = preparePredicates(cb, productRoot, id, name, manufacturer, targetSpecies, productType, vaccinationType);
        cq.where(predicates.toArray(new Predicate[0]));

        List<Product> productList = entityManager.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return productList;
    }


    @Override
    public Product save(Product newProduct) {
        User user = userDetailServiceImpl.getAuthenticatedUser();
        newProduct.setUser(user);

        SubscriptionPlan plan = SubscriptionPlan.fromPlanId(user.getPlanId());
        if (plan == SubscriptionPlan.NO_PLAN) {
            checkProductLimit(user);
        }

        return saveProductByType(newProduct);
    }

    @Transactional
    public Product update(Long id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        updatedProduct.setId(existingProduct.getId());
        return updateProductByType(existingProduct, updatedProduct);
    }

    private List<Predicate> preparePredicates(CriteriaBuilder cb, Root<Product> productRoot, Long id, String name, String manufacturer, String targetSpecies, ProductType productType, String vaccinationType) {
        User user = userDetailServiceImpl.getAuthenticatedUser();
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(productRoot.get("user"), user));

        if (id != null) {
            predicates.add(cb.equal(productRoot.get("id"), id));
        }
        if (name != null) {
            predicates.add(cb.like(productRoot.get("name"), "%" + name + "%"));
        }
        if (manufacturer != null) {
            predicates.add(cb.like(productRoot.get("manufacturer"), "%" + manufacturer + "%"));
        }
        if (productType != null) {
            predicates.add(cb.equal(productRoot.get("productType"), productType));
        }
        if (targetSpecies != null) {
            predicates.add(cb.like(productRoot.get("targetSpecies"), "%" + targetSpecies + "%"));
        }
        if (vaccinationType != null) {
            predicates.add(cb.like(productRoot.get("type"), "%" + vaccinationType + "%"));
        }

        return predicates;
    }

    private Product saveProductByType(Product newProduct) {
        ProductType productType = newProduct.getProductType();
        if (productType == null) {
            throw new IllegalArgumentException("Product type is required");
        }

        switch (productType) {
            case VACCINATION:
                Vaccination vaccination = (Vaccination) newProduct;
                validateVaccinationProperties(vaccination);
                break;
            case SERVICE:
            case PETCAREITEM:
            case MEDICATION:
            case FOOD:
                break;
            default:
                throw new IllegalArgumentException("Invalid product type: " + productType);
        }

        return productRepository.save(newProduct);
    }

    @Transactional
    private Product updateProductByType(Product existingProduct, Product updatedProduct) {
        ProductType existingProductType = existingProduct.getProductType();
        ProductType updatedProductType = updatedProduct.getProductType();

        if (existingProductType != updatedProductType) {
            throw new IllegalArgumentException("Product type cannot be changed");
        }

        if (updatedProductType == null) {
            throw new IllegalArgumentException("Product type is required");
        }

        switch (updatedProductType) {
            case VACCINATION:
                return updateVaccinationProduct(existingProduct, updatedProduct);
            case SERVICE:
            case PETCAREITEM:
            case MEDICATION:
            case FOOD:
                return updateProductFields(existingProduct, updatedProduct);
            default:
                throw new IllegalArgumentException("Invalid product type: " + updatedProductType);
        }
    }

    private Vaccination updateVaccinationProduct(Product existingProduct, Product updatedProduct) {
        if (!(updatedProduct instanceof Vaccination)) {
            throw new ClassCastException("Cannot cast to Vaccination type");
        }

        Vaccination updatedVaccination = (Vaccination) updatedProduct;
        Vaccination existingVaccination = (Vaccination) existingProduct;

        validateVaccinationProperties(updatedVaccination);
        existingVaccination.setType(updatedVaccination.getType());

        return (Vaccination) updateProductFields(existingVaccination, updatedVaccination);
    }


    private Product updateProductFields(Product existingProduct, Product updatedProduct) {
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setManufacturer(updatedProduct.getManufacturer());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setTargetSpecies(updatedProduct.getTargetSpecies());
        existingProduct.setProductType(updatedProduct.getProductType());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setCost(updatedProduct.getCost());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setImageSrc(updatedProduct.getImageSrc());

        return existingProduct;
    }

    private void validateVaccinationProperties(Vaccination vaccination) {
        if (vaccination.getType() == null) {
            throw new IllegalArgumentException("Vaccine type is required for Vaccination");
        }
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new CustomException("Product with id " + id + " not found."));
    }

    private void checkProductLimit(User user) {
        int currentProductCount = productRepository.countByUser(user);
        if (currentProductCount >= 100) {
            throw new CustomException("You have reached the maximum number of products allowed for your subscription plan.");
        }
    }

    @Override
    public boolean deleteById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            productRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
