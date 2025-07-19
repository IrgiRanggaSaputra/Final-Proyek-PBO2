package com.mycompany.peminjamanbarang.service;

import com.mycompany.peminjamanbarang.model.BaseEntity;
import java.util.List;

/**
 * Abstract Service class - Abstraction dan Template Method Pattern
 * Menyediakan template untuk business logic operations
 */
public abstract class BaseService<T extends BaseEntity, ID> {
    
    // Abstract methods yang harus diimplementasi oleh subclass - Polymorphism
    protected abstract void validateBeforeSave(T entity);
    protected abstract void validateBeforeUpdate(T entity);
    protected abstract void performBusinessLogicAfterSave(T entity);
    protected abstract void performBusinessLogicAfterUpdate(T entity);
    
    // Template method - Abstraction
    public final void save(T entity) {
        // Pre-validation
        if (entity == null) {
            throw new IllegalArgumentException("Entity tidak boleh null");
        }
        
        // Validate entity using its own method (Polymorphism)
        if (!entity.isValid()) {
            throw new IllegalArgumentException("Entity tidak valid: " + entity.getDisplayInfo());
        }
        
        // Custom validation by subclass
        validateBeforeSave(entity);
        
        // Perform save operation
        doSave(entity);
        
        // Post-save business logic
        performBusinessLogicAfterSave(entity);
    }
    
    // Template method - Abstraction
    public final void update(T entity) {
        // Pre-validation
        if (entity == null) {
            throw new IllegalArgumentException("Entity tidak boleh null");
        }
        
        if (!entity.hasValidId()) {
            throw new IllegalArgumentException("Entity harus memiliki ID valid untuk update");
        }
        
        // Validate entity using its own method (Polymorphism)
        if (!entity.isValid()) {
            throw new IllegalArgumentException("Entity tidak valid: " + entity.getDisplayInfo());
        }
        
        // Custom validation by subclass
        validateBeforeUpdate(entity);
        
        // Perform update operation
        doUpdate(entity);
        
        // Post-update business logic
        performBusinessLogicAfterUpdate(entity);
    }
    
    // Abstract methods untuk CRUD operations
    protected abstract void doSave(T entity);
    protected abstract void doUpdate(T entity);
    protected abstract void doDelete(ID id);
    protected abstract T doFindById(ID id);
    protected abstract List<T> doFindAll();
    
    // Public methods yang menggunakan template
    public final void delete(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID tidak boleh null");
        }
        doDelete(id);
    }
    
    public final T findById(ID id) {
        if (id == null) {
            return null;
        }
        return doFindById(id);
    }
    
    public final List<T> findAll() {
        return doFindAll();
    }
}
