package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService
{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories()
    {
        // Fetch all Category entities from database
        List<Category> categories = categoryRepository.findAll();

        // If no data found, throw custom exception i.e validation
        if(categories.isEmpty())
        {
            throw new APIException("No category created till now.");
        }

        /*
           Convert List<Category> (Entity) -> List<CategoryDTO> (DTO)
           Using ModelMapper to avoid exposing entity directly to client
        */
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        // Create response object to wrap DTO list (good practice for APIs)
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        return categoryResponse;
    }

    @Override
    public void createCategory(Category category)
    {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null)
        {
            throw new APIException("Category with name " + category.getCategoryName() + " already exists !!!");
        }
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId)
    {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "categoryId", categoryId));

        categoryRepository.delete(category);
        return "category with categoryId: " + categoryId + " deleted successfully !!!";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId)
    {
        Optional<Category>savedCategoryOptional = categoryRepository.findById(categoryId);

        Category savedCategory = savedCategoryOptional
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);
        return savedCategory;
    }
}
