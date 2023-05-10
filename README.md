# FileViewer

Тестовое задание от VK

## Используемые библиотеки и технологии

* БД: Room
* Асихронные операции: Coroutines, Livedata
* DI: Dagger Hilt
* Работа со списками: ListAdapter и DiffUtilCallback

Приложение разбито на 3 слоя: data, domain и presentation. Presentation слой реализован с помощью паттерна MVVM

## Скрины
При запуске приложения файлы, хэшей которых нет в базе подсвечиваются зеленым (при первом запуске это все файлы)

На левой картинке экран при первом запуске, на правой - второй 

<img src="https://github.com/sevagrbnv/FileViewer/assets/65513466/f986dab3-4458-4542-b377-9968afc446e5" width="250">
<img src="https://github.com/sevagrbnv/FileViewer/assets/65513466/ba703315-4b99-49bf-a1b7-22cf860956f4" width="250">

Содержимое списка можно сортировать по параметрам из верхнего меню. На правой картинке продемонстирована сортировка по дате (сначала старые)

<img src="https://github.com/sevagrbnv/FileViewer/assets/65513466/9163f9a1-f728-4299-b42d-7ae6a0e8d32d" width="250">
<img src="https://github.com/sevagrbnv/FileViewer/assets/65513466/f85c256a-0854-4c14-b113-409c2d752050" width="250">

При нажатии на элемент списка, если это папка, то показывается ее содержимое, если файл, то открывается файл

<img src="https://github.com/sevagrbnv/FileViewer/assets/65513466/584adde7-8c2b-41c1-b1bb-fe04628e681e" width="250">
<img src="https://github.com/sevagrbnv/FileViewer/assets/65513466/f3072502-4b71-4fb0-8605-adf05ce79e84" width="250">

У файлов есть выпадающее меню, на данный момент в нем только возможность поделиться файлом, но при такой организации легче внедрять новый функционал

<img src="https://github.com/sevagrbnv/FileViewer/assets/65513466/14c31850-b9e7-4bba-9671-bfdf701e3ce6" width="250">
<img src="https://github.com/sevagrbnv/FileViewer/assets/65513466/7585aa80-632e-472b-8d40-6f3585d983a0" width="250">

