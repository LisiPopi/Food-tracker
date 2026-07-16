package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class MainWebController {

    private final IngredientRepository ingredientRepository;
    private final MealLogRepository mealLogRepository;
    private final UserRepository userRepository;
    private final MealTypeRepository mealTypeRepository;
    private final CategoryRepository categoryRepository; // Додали репозиторій категорій

    public MainWebController(IngredientRepository ingredientRepository,
                             MealLogRepository mealLogRepository,
                             UserRepository userRepository,
                             MealTypeRepository mealTypeRepository,
                             CategoryRepository categoryRepository) {
        this.ingredientRepository = ingredientRepository;
        this.mealLogRepository = mealLogRepository;
        this.userRepository = userRepository;
        this.mealTypeRepository = mealTypeRepository;
        this.categoryRepository = categoryRepository;
    }

    // --- 1. СТОРІНКА ЩОДЕННИКА (ГОЛОВНА) ---
    @GetMapping("/")
    public String index(@RequestParam(required = false) String date, Model model) {
        LocalDate selectedDate = (date != null && !date.isEmpty()) ? LocalDate.parse(date) : LocalDate.now();

        List<Ingredient> ingredients = ingredientRepository.findAll();
        List<MealLog> logs = mealLogRepository.findByLogDate(selectedDate);

        double totalCal = 0, totalProt = 0, totalFat = 0, totalCarbs = 0;
        for (MealLog log : logs) {
            double multiplier = log.getWeight() / 100.0;
            totalCal += log.getIngredient().getCalories() * multiplier;
            totalProt += log.getIngredient().getProtein() * multiplier;
            totalFat += log.getIngredient().getFat() * multiplier;
            totalCarbs += log.getIngredient().getCarbs() * multiplier;
        }

        model.addAttribute("currentDate", selectedDate.toString());
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("logs", logs);
        model.addAttribute("totalCal", Math.round(totalCal));
        model.addAttribute("totalProt", Math.round(totalProt * 10.0) / 10.0);
        model.addAttribute("totalFat", Math.round(totalFat * 10.0) / 10.0);
        model.addAttribute("totalCarbs", Math.round(totalCarbs * 10.0) / 10.0);

        return "index";
    }

    // --- 2. СТОРІНКА БАЗИ ПРОДУКТІВ ---
    @GetMapping("/ingredients")
    public String ingredientsPage(Model model) {
        // Якщо категорій ще немає, створюємо базовий набір
        if (categoryRepository.count() == 0) {
            String[] defaultCategories = {
                    "М'ясо та птиця", "Молочні продукти", "Овочі та зелень",
                    "Фрукти та ягоди", "Крупи та гарніри", "Напої", "Солодощі", "Загальна"
            };
            for (String catName : defaultCategories) {
                Category cat = new Category();
                cat.setName(catName);
                categoryRepository.save(cat);
            }
        }

        model.addAttribute("ingredients", ingredientRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "ingredients";
    }

    // --- ОБРОБНИКИ ДАНИХ (POST) ---
    @PostMapping("/web/add-log")
    public String addLog(@RequestParam String date, @RequestParam String mealType,
                         @RequestParam Long ingredientId, @RequestParam Double grams) {

        Ingredient food = ingredientRepository.findById(ingredientId).orElseThrow();

        MealType type = mealTypeRepository.findAll().stream()
                .filter(m -> m.getName().equals(mealType))
                .findFirst()
                .orElseGet(() -> {
                    MealType newType = new MealType();
                    newType.setName(mealType);
                    return mealTypeRepository.save(newType);
                });

        User user = userRepository.findById(1L).orElseGet(() -> {
            User newUser = new User();
            newUser.setUsername("Студент");
            return userRepository.save(newUser);
        });

        MealLog log = new MealLog();
        log.setLogDate(LocalDate.parse(date));
        log.setWeight(grams);
        log.setIngredient(food);
        log.setMealType(type);
        log.setUser(user);

        mealLogRepository.save(log);

        return "redirect:/?date=" + date;
    }

    @PostMapping("/web/add-ingredient")
    public String addIngredient(@RequestParam String name, @RequestParam Double calories,
                                @RequestParam Double protein, @RequestParam Double fat,
                                @RequestParam Double carbs, @RequestParam Long categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow();

        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setCalories(calories);
        ingredient.setProtein(protein);
        ingredient.setFat(fat);
        ingredient.setCarbs(carbs);
        ingredient.setCategory(category);

        ingredientRepository.save(ingredient);

        return "redirect:/ingredients";
    }


    @PostMapping("/web/delete-ingredient")
    public String deleteIngredient(@RequestParam Long id) {
        try {
            ingredientRepository.deleteById(id);
        } catch (Exception e) {

            return "redirect:/ingredients?error=in_use";
        }
        return "redirect:/ingredients";
    }

    @PostMapping("/web/update-ingredient")
    public String updateIngredient(@RequestParam Long id, @RequestParam String name,
                                   @RequestParam Double calories, @RequestParam Double protein,
                                   @RequestParam Double fat, @RequestParam Double carbs,
                                   @RequestParam Long categoryId) {
        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow();
        Category category = categoryRepository.findById(categoryId).orElseThrow();

        ingredient.setName(name);
        ingredient.setCalories(calories);
        ingredient.setProtein(protein);
        ingredient.setFat(fat);
        ingredient.setCarbs(carbs);
        ingredient.setCategory(category);

        ingredientRepository.save(ingredient);
        return "redirect:/ingredients";
    }


    @PostMapping("/web/add-category")
    public String addCategory(@RequestParam String name) {
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
        return "redirect:/ingredients";
    }


    @PostMapping("/web/delete-log")
    public String deleteLog(@RequestParam Long id, @RequestParam String date) {
        mealLogRepository.deleteById(id);
        return "redirect:/?date=" + date;
    }


    @PostMapping("/web/update-log")
    public String updateLog(@RequestParam Long id, @RequestParam String date,
                            @RequestParam String mealType, @RequestParam Long ingredientId,
                            @RequestParam Double grams) {

        MealLog log = mealLogRepository.findById(id).orElseThrow();
        Ingredient food = ingredientRepository.findById(ingredientId).orElseThrow();

        MealType type = mealTypeRepository.findAll().stream()
                .filter(m -> m.getName().equals(mealType))
                .findFirst()
                .orElseGet(() -> {
                    MealType newType = new MealType();
                    newType.setName(mealType);
                    return mealTypeRepository.save(newType);
                });

        log.setWeight(grams);
        log.setIngredient(food);
        log.setMealType(type);

        mealLogRepository.save(log);

        return "redirect:/?date=" + date;
    }

    // --- ЕКСПОРТ В EXCEL З ФІЛЬТРАЦІЄЮ ТА МОВОЮ ---
    @GetMapping("/web/export-excel")
    public org.springframework.http.ResponseEntity<byte[]> exportToExcel(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false, defaultValue = "uk") String lang
    ) throws java.io.IOException {

        List<MealLog> allLogs = mealLogRepository.findAll();
        List<MealLog> filteredLogs = allLogs;

        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            java.time.LocalDate start = java.time.LocalDate.parse(startDate);
            java.time.LocalDate end = java.time.LocalDate.parse(endDate);

            filteredLogs = allLogs.stream().filter(log -> {
                java.time.LocalDate logDate = log.getLogDate();
                return !logDate.isBefore(start) && !logDate.isAfter(end);
            }).collect(java.util.stream.Collectors.toList());
        }

        org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();

        // Переклад назви листа
        String sheetName = "Щоденник Харчування";
        if ("en".equals(lang)) sheetName = "Meal Diary";
        if ("de".equals(lang)) sheetName = "Ernährungstagebuch";

        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet(sheetName);

        // Переклад заголовків колонок
        String[] headers = {"Дата", "Прийом їжі", "Продукт", "Вага (г)", "Ккал", "Білки", "Жири", "Вуглеводи"};
        if ("en".equals(lang)) {
            headers = new String[]{"Date", "Meal Type", "Product", "Weight (g)", "Kcal", "Protein", "Fat", "Carbs"};
        } else if ("de".equals(lang)) {
            headers = new String[]{"Datum", "Mahlzeit", "Produkt", "Gewicht (g)", "Kcal", "Eiweiß", "Fett", "Kohlenhydrate"};
        }

        org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (MealLog log : filteredLogs) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);

            // Переклад типів прийому їжі (Сніданок -> Breakfast тощо)
            String mealTypeName = log.getMealType().getName();
            if ("en".equals(lang)) {
                switch (mealTypeName) {
                    case "Сніданок": mealTypeName = "Breakfast"; break;
                    case "Обід": mealTypeName = "Lunch"; break;
                    case "Вечеря": mealTypeName = "Dinner"; break;
                    case "Перекус": mealTypeName = "Snack"; break;
                }
            } else if ("de".equals(lang)) {
                switch (mealTypeName) {
                    case "Сніданок": mealTypeName = "Frühstück"; break;
                    case "Обід": mealTypeName = "Mittagessen"; break;
                    case "Вечеря": mealTypeName = "Abendessen"; break;
                    case "Перекус": mealTypeName = "Snack"; break;
                }
            }

            row.createCell(0).setCellValue(log.getLogDate().toString());
            row.createCell(1).setCellValue(mealTypeName); // Записуємо перекладений прийом їжі
            row.createCell(2).setCellValue(log.getIngredient().getName());
            row.createCell(3).setCellValue(log.getWeight());

            double multiplier = log.getWeight() / 100.0;
            row.createCell(4).setCellValue(log.getIngredient().getCalories() * multiplier);
            row.createCell(5).setCellValue(log.getIngredient().getProtein() * multiplier);
            row.createCell(6).setCellValue(log.getIngredient().getFat() * multiplier);
            row.createCell(7).setCellValue(log.getIngredient().getCarbs() * multiplier);
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        org.springframework.http.HttpHeaders responseHeaders = new org.springframework.http.HttpHeaders();
        responseHeaders.setContentType(org.springframework.http.MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        String fileName = "Report_" + startDate + "_to_" + endDate + ".xlsx";
        responseHeaders.setContentDispositionFormData("attachment", fileName);

        return org.springframework.http.ResponseEntity.ok()
                .headers(responseHeaders)
                .body(outputStream.toByteArray());
    }
}