# Правила оформления и ведения документации

Документы создаются в формате Markdown.

### Структура

Существуют две категории документов:

- Документы общего дерева - общая информация, без конкретной реализации.
Лежат в корневой директории docs. Содержат ссылки на модули.

- Документация к конкретным модулям - описывает сам модуль. Лежит в директории
docs у модуля. Может ссылаться на статьи из основного дерева

Идея документов основного дерева - описать подход и ответить
на вопрос зачем использовать. Некоторые статьи могут
объединять в себе ссылки на несколько модулей.

Идея документации модулей - документация модулей. Как использовать, какие классы.
Ссылается на конкретные классы из своего модуля.

Общая разводная содержится в [main][main.md].