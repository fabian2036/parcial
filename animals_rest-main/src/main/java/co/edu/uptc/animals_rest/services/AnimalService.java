package co.edu.uptc.animals_rest.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import co.edu.uptc.animals_rest.exception.InvalidRangeException;
import co.edu.uptc.animals_rest.models.Animal;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

@Service
public class AnimalService {
    private static final Logger logger = LoggerFactory.getLogger(AnimalService.class);
    @Value("${animal.file.path}")
    private String filePath;

    public List<Animal> getAnimalInRange(int from, int to) throws IOException {
        List<String> listAnimal = Files.readAllLines(Paths.get(filePath));
        List<Animal> animales = new ArrayList<>();

        if (from < 0 || to >= listAnimal.size() || from > to) {
            logger.warn("Invalid range: Please check the provided indices. Range: 0 to {}", listAnimal.size());
            throw new InvalidRangeException("Invalid range: Please check the provided indices.");
        }

        for (String line : listAnimal) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                String categoria = parts[0].trim();
                String nombre = parts[1].trim();
                animales.add(new Animal(nombre, categoria));
            }
        }

        return animales.subList(from, to + 1);
    }

    public List<Animal> getAnimalAll() throws IOException {
        List<String> listAnimal = Files.readAllLines(Paths.get(filePath));
        List<Animal> animales = new ArrayList<>();

        for (String line : listAnimal) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                String category = parts[0].trim();
                String name = parts[1].trim();
                animales.add(new Animal(name, category));
            }
        }

        return animales;
    }

    public List<Animal> getAnimalCat(String categoryIpn) throws IOException {
        List<String> listAnimal = Files.readAllLines(Paths.get(filePath));
        List<Animal> animales = new ArrayList<>();

        for (String line : listAnimal) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                String category = parts[0].trim();
                String name = parts[1].trim();
                if (category.equals(categoryIpn)) // Compare with IPN category only
                    animales.add(new Animal(name, category));
            }
        }
        return animales;
    }

    public List<Animal> getAnimalLength(int length) throws IOException {
        List<String> listAnimal = Files.readAllLines(Paths.get(filePath));
        List<Animal> animales = new ArrayList<>();

        for (String line : listAnimal) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                String category = parts[0].trim();
                String name = parts[1].trim();
                if (name.length() < length) // Compare with IPN category only
                    animales.add(new Animal(name, category));
            }
        }
        return animales;
    }

    public List<Map<String, Object>> getAnimalByCategory() throws IOException {
        List<String> listAnimal = Files.readAllLines(Paths.get(filePath));
        Map<String, Integer> categoryCount = new HashMap<>();

        // Contar la cantidad de animales por categoría
        for (String line : listAnimal) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                String animalCategory = parts[0].trim();
                // Sumar al conteo de la categoría actual
                categoryCount.put(animalCategory, categoryCount.getOrDefault(animalCategory, 0) + 1);
            }
        }

        // Convertir los resultados en la lista de mapas esperada
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : categoryCount.entrySet()) {
            Map<String, Object> categoryData = new HashMap<>();
            categoryData.put("category", entry.getKey());
            categoryData.put("number", entry.getValue());
            result.add(categoryData);
        }

        return result; // Retornar la lista de mapas con el formato JSON
    }

}
