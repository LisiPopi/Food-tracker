const translations = {
    uk: { title: "Щоденник Харчування", cal: "Калорії", prot: "Білки", fat: "Жири", carbs: "Вуглеводи", breakfast: "Сніданок", lunch: "Обід", dinner: "Вечеря", snack: "Перекус", selectFood: "-- Оберіть продукт з бази --", addBtn: "✔️ Записати", time: "Прийом їжі", name: "Продукт", weight: "Вага (г)", theme: "Темна тема", themeL: "Світла тема", goal: "Ціль", kcalText: "ккал", promptGoal: "Введіть вашу нову ціль (ккал):", createNew: "Створити", gramsPh: "Грами", modalTitle: "Новий продукт", saveToDB: "💾 Зберегти", phName: "Назва (напр. Банан)", phCal: "Ккал (на 100г)", phProt: "Білки (г)", phFat: "Жири (г)", phCarb: "Вуглеводи (г)", actions: "Дії", editProd: "Редагувати", updateBtn: "💾 Оновити", from: "З:", to: "До:", phDate: "Оберіть...", exportBtn: "📊 Експорт Excel", navDiary: "📅 Щоденник", navBase: "🍎 База продуктів", shortProt: "Б", shortFat: "Ж", shortCarbs: "В", gramUnit: "г",
        categoryHeader: "Категорія", "М'ясо та птиця": "М'ясо та птиця", "Молочні продукти": "Молочні продукти", "Овочі та зелень": "Овочі та зелень", "Фрукти та ягоди": "Фрукти та ягоди", "Крупи та гарніри": "Крупи та гарніри", "Напої": "Напої", "Солодощі": "Солодощі", "Загальна": "Загальна",
        "Сніданок": "Сніданок", "Обід": "Обід", "Вечеря": "Вечеря", "Перекус": "Перекус" },

    en: { title: "Meal Diary", cal: "Calories", prot: "Protein", fat: "Fat", carbs: "Carbs", breakfast: "Breakfast", lunch: "Lunch", dinner: "Dinner", snack: "Snack", selectFood: "-- Select product from base --", addBtn: "✔️ Log Meal", time: "Meal Type", name: "Product", weight: "Weight (g)", theme: "Dark Theme", themeL: "Light Theme", goal: "Goal", kcalText: "kcal", promptGoal: "Enter your new calorie goal:", createNew: "Create", gramsPh: "Grams", modalTitle: "New Product", saveToDB: "💾 Save", phName: "Name (e.g. Banana)", phCal: "Kcal (per 100g)", phProt: "Protein (g)", phFat: "Fat (g)", phCarb: "Carbs (g)", actions: "Actions", editProd: "Edit", updateBtn: "💾 Update", from: "From:", to: "To:", phDate: "Select...", exportBtn: "📊 Export Excel", navDiary: "📅 Diary", navBase: "🍎 Food Base", shortProt: "P", shortFat: "F", shortCarbs: "C", gramUnit: "g",
        categoryHeader: "Category", "М'ясо та птиця": "Meat & Poultry", "Молочні продукти": "Dairy", "Овочі та зелень": "Vegetables & Greens", "Фрукти та ягоди": "Fruits & Berries", "Крупи та гарніри": "Grains & Sides", "Напої": "Beverages", "Солодощі": "Sweets", "Загальна": "General",
        "Сніданок": "Breakfast", "Обід": "Lunch", "Вечеря": "Dinner", "Перекус": "Snack" },

    de: { title: "Ernährungstagebuch", cal: "Kalorien", prot: "Eiweiß", fat: "Fett", carbs: "Kohlenhydrate", breakfast: "Frühstück", lunch: "Mittagessen", dinner: "Abendessen", snack: "Snack", selectFood: "-- Produkt aus der Basis wählen --", addBtn: "✔️ Eintragen", time: "Mahlzeit", name: "Produkt", weight: "Gewicht (g)", theme: "Dunkles Design", themeL: "Helles Design", goal: "Ziel", kcalText: "kcal", promptGoal: "Neues Kalorienziel eingeben:", createNew: "Erstellen", gramsPh: "Gramm", modalTitle: "Neues Produkt", saveToDB: "💾 Speichern", phName: "Name (z.B. Banane)", phCal: "Kcal (pro 100g)", phProt: "Eiweiß (g)", phFat: "Fett (g)", phCarb: "Kohlenhydrate (g)", actions: "Aktionen", editProd: "Bearbeiten", updateBtn: "💾 Aktualisieren", from: "Von:", to: "Bis:", phDate: "Wählen...", exportBtn: "📊 Excel Export", navDiary: "📅 Tagebuch", navBase: "🍎 Produktdatenbank", shortProt: "E", shortFat: "F", shortCarbs: "K", gramUnit: "g",
        categoryHeader: "Kategorie", "М'ясо та птиця": "Fleisch & Geflügel", "Молочні продукти": "Milchprodukte", "Овочі та зелень": "Gemüse & Kräuter", "Фрукти та ягоди": "Obst & Beeren", "Крупи та гарніри": "Getreide & Beilagen", "Напої": "Getränke", "Солодощі": "Süßigkeiten", "Загальна": "Allgemein",
        "Сніданок": "Frühstück", "Обід": "Mittagessen", "Вечеря": "Abendessen", "Перекус": "Snack" }
};

let currentLang = localStorage.getItem('lang') || 'uk';
const langSwitch = document.getElementById('langSwitch');
if (langSwitch) langSwitch.value = currentLang;

// --- Ініціалізація головного календаря ---
const calElement = document.getElementById('calendar');
let calendarInst = null;
if (calElement) {
    calendarInst = flatpickr("#calendar", {
        dateFormat: "Y-m-d",
        locale: currentLang === 'en' ? 'default' : currentLang,
        onChange: function(selectedDates, dateStr) {
            window.location.href = '/?date=' + dateStr;
        }
    });
}

// --- Ініціалізація календарів для Excel ---
document.addEventListener('DOMContentLoaded', function() {
    flatpickr("#startDate", {
        dateFormat: "Y-m-d",
        disableMobile: "true",
        locale: currentLang === 'en' ? 'default' : currentLang
    });

    flatpickr("#endDate", {
        dateFormat: "Y-m-d",
        disableMobile: "true",
        locale: currentLang === 'en' ? 'default' : currentLang
    });
});

function changeLanguage() {
    const sw = document.getElementById('langSwitch');
    if (sw) currentLang = sw.value;
    localStorage.setItem('lang', currentLang);

    // --- Оновлюємо прихований інпут для Excel ---
    const exportLangInput = document.getElementById('exportLangInput');
    if (exportLangInput) exportLangInput.value = currentLang;

    document.querySelectorAll('[data-i18n]').forEach(el => {
        const translation = translations[currentLang][el.getAttribute('data-i18n')];
        if (translation) el.innerText = translation;
    });

    document.querySelectorAll('[data-i18n-ph]').forEach(el => {
        const translation = translations[currentLang][el.getAttribute('data-i18n-ph')];
        if (translation) el.placeholder = translation;
    });

    document.querySelectorAll('.dynamic-ing-option').forEach(el => {
        const name = el.getAttribute('data-name');
        const cal = el.getAttribute('data-cal');
        const kcalStr = translations[currentLang].kcalText;
        el.innerText = `${name} (${cal} ${kcalStr})`;
    });

    updateThemeText();
    if (calendarInst) calendarInst.set("locale", currentLang === 'en' ? 'default' : currentLang);
}

const themeBtn = document.getElementById('themeToggle');
if (localStorage.getItem('theme') === 'dark') document.body.setAttribute('data-theme', 'dark');

function updateThemeText() {
    const isDark = document.body.getAttribute('data-theme') === 'dark';
    const icon = document.getElementById('themeIcon');
    if (icon) icon.innerText = isDark ? '☀️' : '🌙';

    const themeTextEl = document.querySelector('[data-i18n^="theme"]');
    if (themeTextEl) themeTextEl.innerText = isDark ? translations[currentLang].themeL : translations[currentLang].theme;
}

if (themeBtn) {
    themeBtn.addEventListener('click', () => {
        const isDark = document.body.getAttribute('data-theme') === 'dark';
        isDark ? document.body.removeAttribute('data-theme') : document.body.setAttribute('data-theme', 'dark');
        localStorage.setItem('theme', isDark ? 'light' : 'dark');
        updateThemeText();
    });
}

let userGoal = localStorage.getItem('calorieGoal') || 2400;
const goalEl = document.getElementById('goalValue');
if (goalEl) goalEl.innerText = userGoal;

function editGoal() {
    let newGoal = prompt(translations[currentLang].promptGoal, userGoal);
    if (newGoal && !isNaN(newGoal)) {
        userGoal = newGoal; localStorage.setItem('calorieGoal', userGoal);
        const gEl = document.getElementById('goalValue');
        if (gEl) gEl.innerText = userGoal;
        updateProgressBar();
    }
}

function updateProgressBar() {
    const totCalEl = document.getElementById('totCal');
    if (!totCalEl) return;

    let totalCal = parseFloat(totCalEl.innerText);
    let percent = (totalCal / userGoal) * 100;
    const progressBar = document.getElementById('calProgress');

    if (percent > 100) {
        progressBar.style.width = '100%';
        progressBar.style.backgroundColor = 'var(--danger)';
        totCalEl.style.color = 'var(--danger)';
    } else {
        progressBar.style.width = percent + '%';
        progressBar.style.backgroundColor = 'var(--table-header)';
        totCalEl.style.color = 'var(--table-header)';
    }
}

function openModal() {
    const mod = document.getElementById('addIngModal');
    if (mod) mod.classList.add('active');
}
function closeModal() {
    const mod = document.getElementById('addIngModal');
    if (mod) mod.classList.remove('active');
}

// --- НОВІ ФУНКЦІЇ ДЛЯ МОДАЛЬНОГО ВІКНА РЕДАГУВАННЯ ---
function openEditLogModal(btn) {
    const mod = document.getElementById('editLogModal');
    if (mod) {
        document.getElementById('editLogId').value = btn.getAttribute('data-id');
        document.getElementById('editLogMealType').value = btn.getAttribute('data-meal');
        document.getElementById('editLogIngredient').value = btn.getAttribute('data-ing');
        document.getElementById('editLogWeight').value = btn.getAttribute('data-weight');
        mod.classList.add('active');
    }
}

function closeEditLogModal() {
    const mod = document.getElementById('editLogModal');
    if (mod) mod.classList.remove('active');
}

// Запуск при завантаженні
changeLanguage();
updateProgressBar();