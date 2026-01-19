package com.sleeplessdog.matchthewords.score.domain.models

import com.sleeplessdog.matchthewords.R
import com.sleeplessdog.matchthewords.score.models.AwardMeta

object AwardsCatalog {

    val all: List<AwardMeta> = listOf(

        AwardMeta(
            id = AwardId.MAMMOTH_HUNTER,
            title = "Охотник на мамонтов",
            description = "Выучил всех млекопитающих",
            iconLocked = R.drawable.award_mamoth_off,
            iconUnlocked = R.drawable.award_mamoth_on
        ),

        AwardMeta(
            id = AwardId.RESPONSIBLE_CITIZEN,
            title = "Ответственный гражданин",
            description = "Сообщил об ошибке 10 раз",
            iconLocked = R.drawable.award_responsibility_off,
            iconUnlocked = R.drawable.award_responsibility_on
        ),

        AwardMeta(
            id = AwardId.PERFECTIONIST,
            title = "Перфекционист",
            description = "Закрыл категорию на 100%",
            iconLocked = R.drawable.award_perfectionist_off,
            iconUnlocked = R.drawable.award_perfectionist_on
        ),

        AwardMeta(
            id = AwardId.ALMOST,
            title = "Ну почти",
            description = "Закрыл категорию на 99%",
            iconLocked = R.drawable.award_almost_off,
            iconUnlocked = R.drawable.award_almost_on
        ),

        AwardMeta(
            id = AwardId.NOW_FOR_SURE,
            title = "Ну теперь точно",
            description = "Довёл категорию с 95–99% до 100%",
            iconLocked = R.drawable.award_for_sure_off,
            iconUnlocked = R.drawable.award_for_sure_on
        ),

        AwardMeta(
            id = AwardId.ALMOST_EXPERT,
            title = "Почти эксперт",
            description = "Закрыл 5 категорий минимум на 90%",
            iconLocked = R.drawable.award_almost_an_expert_off,
            iconUnlocked = R.drawable.award_almost_an_expert_on
        ),

        AwardMeta(
            id = AwardId.KNOWLEDGE_COLLECTOR,
            title = "Коллекционер знаний",
            description = "Открыл все слова в категории",
            iconLocked = R.drawable.collector_of_knowledge_off,
            iconUnlocked = R.drawable.collector_of_knowledge_on
        ),

        AwardMeta(
            id = AwardId.FOR_A_RAINY_DAY,
            title = "На чёрный день",
            description = "Добавил 50 слов в избранное",
            iconLocked = R.drawable.award_rainy_off,
            iconUnlocked = R.drawable.award_rainy_on
        ),

        AwardMeta(
            id = AwardId.MAYBE_USEFUL,
            title = "А вдруг пригодится",
            description = "Добавил слово в избранное и ни разу к нему не вернулся",
            iconLocked = R.drawable.award_what_if_it_comes_in_handy_off,
            iconUnlocked = R.drawable.award_what_if_it_comes_in_handy_on
        ),

        AwardMeta(
            id = AwardId.I_LIVE_HERE,
            title = "Я тут живу",
            description = "Заходил в приложение 7 дней подряд",
            iconLocked = R.drawable.award_live_here_off,
            iconUnlocked = R.drawable.award_live_here_on
        ),

        AwardMeta(
            id = AwardId.LITTLE_BUT_REGULAR,
            title = "Чуть-чуть, но регулярно",
            description = "Учился 3 дня подряд меньше 5 минут",
            iconLocked = R.drawable.award_a_little_bit_but_regularly_off,
            iconUnlocked = R.drawable.award_a_little_bit_but_regularly_on
        ),

        AwardMeta(
            id = AwardId.LAZY_PANDA,
            title = "Ленивая панда",
            description = "Сделал урок из 3 слов",
            iconLocked = R.drawable.award_panda_off,
            iconUnlocked = R.drawable.award_panda_on
        ),

        AwardMeta(
            id = AwardId.I_TRIED,
            title = "Я старался",
            description = "Сделал урок и сразу вышел",
            iconLocked = R.drawable.award_my_best_off,
            iconUnlocked = R.drawable.award_my_best_on
        ),

        AwardMeta(
            id = AwardId.WORD_BY_WORD,
            title = "Слово за слово",
            description = "Выучил 100 слов",
            iconLocked = R.drawable.award_word_for_word_off,
            iconUnlocked = R.drawable.award_word_for_word_on
        ),

        AwardMeta(
            id = AwardId.HONESTLY,
            title = "Не идеально, но честно",
            description = "Выучил слово со второй попытки",
            iconLocked = R.drawable.award_honestly_off,
            iconUnlocked = R.drawable.award_honestly_on
        ),

        AwardMeta(
            id = AwardId.SLOW_BUT_PRETTY,
            title = "Медленно, зато красиво",
            description = "Отвечал правильно, но дольше обычного",
            iconLocked = R.drawable.award_slow_off,
            iconUnlocked = R.drawable.award_slow_on
        ),

        AwardMeta(
            id = AwardId.SELF_IMPROVEMENT,
            title = "Работа над собой",
            description = "Исправил 10 ранее ошибочных слов",
            iconLocked = R.drawable.award_improvement_off,
            iconUnlocked = R.drawable.award_improvement_on
        ),

        AwardMeta(
            id = AwardId.I_UNDERSTOOD,
            title = "Я понял, честно",
            description = "Ответил правильно после 3 ошибок",
            iconLocked = R.drawable.award_expirience_off,
            iconUnlocked = R.drawable.award_expirience_on
        ),

        AwardMeta(
            id = AwardId.LEARNED_FROM_MISTAKES,
            title = "Учёл прошлый опыт",
            description = "Больше не ошибался в слове после его исправления",
            iconLocked = R.drawable.award_regular_off,
            iconUnlocked = R.drawable.award_regular_on
        ),

        AwardMeta(
            id = AwardId.FIGURED_OUT,
            title = "Разобрался",
            description = "Закрыл все ошибки в одной категории",
            iconLocked = R.drawable.award_figured_it_out_off,
            iconUnlocked = R.drawable.award_figured_it_out_on
        )
    )
}
