package com.acsalamancaf.dscatalogdto;

import java.io.Serializable;

import com.acsalamancaf.dscatalog.entities.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;

	public CategoryDTO(Category entity) {
		this.id = entity.getId();
		this.name = entity.getName();
	}
}
