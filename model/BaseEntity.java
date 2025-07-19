package com.mycompany.peminjamanbarang.model;

/**
 * Abstract class sebagai base untuk semua entity
 * Implementasi Abstraction dan Inheritance
 */
public abstract class BaseEntity {
    protected int id;
    
    // Constructor
    public BaseEntity() {}
    
    public BaseEntity(int id) {
        this.id = id;
    }
    
    // Encapsulation - Getter/Setter
    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }
    
    // Abstract method - harus diimplementasi oleh subclass
    public abstract boolean isValid();
    
    // Abstract method untuk display info
    public abstract String getDisplayInfo();
    
    // Concrete method yang bisa digunakan semua subclass
    public boolean hasValidId() {
        return id > 0;
    }
}
