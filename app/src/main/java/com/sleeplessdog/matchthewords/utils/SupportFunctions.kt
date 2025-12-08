package com.sleeplessdog.matchthewords.utils

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.sleeplessdog.matchthewords.R
import com.sleeplessdog.matchthewords.game.presentation.models.CategoryUi
import com.sleeplessdog.matchthewords.game.presentation.models.DifficultLevel
import com.sleeplessdog.matchthewords.game.presentation.models.Language
import com.sleeplessdog.matchthewords.utils.ConstantsApp.DATE_PATTERN
import com.sleeplessdog.matchthewords.utils.ConstantsApp.SCORE_FILL_CHAR
import com.sleeplessdog.matchthewords.utils.ConstantsApp.SCORE_LENGTH
import com.sleeplessdog.matchthewords.utils.ConstantsGamePrices.GAME_DIFFICULT_EASY
import com.sleeplessdog.matchthewords.utils.ConstantsGamePrices.GAME_DIFFICULT_HARD
import com.sleeplessdog.matchthewords.utils.ConstantsGamePrices.GAME_DIFFICULT_MEDIUM
import com.sleeplessdog.matchthewords.utils.ConstantsGamePrices.LIVES_EASY
import com.sleeplessdog.matchthewords.utils.ConstantsGamePrices.LIVES_EXPERT
import com.sleeplessdog.matchthewords.utils.ConstantsGamePrices.LIVES_HARD
import com.sleeplessdog.matchthewords.utils.ConstantsGamePrices.LIVES_MEDIUM
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object SupportFunctions {

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun getScoreAsString(score: Int): String {
        return score.toString().padStart(SCORE_LENGTH, SCORE_FILL_CHAR)
    }

    fun sortMapByDateDescending(inputMap: Map<String, Int>): Map<String, Int> {
        val dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN)

        return inputMap.mapKeys { entry ->
            LocalDate.parse(
                entry.key, dateFormatter
            )
        } // Преобразуем ключи в LocalDate
            .toSortedMap(compareByDescending { it }) // Сортируем по убыванию дат
            .mapKeys { entry -> entry.key.format(dateFormatter) } // Преобразуем обратно ключи в строковый формат
    }

    fun getGameDifficult(difficultLevel: DifficultLevel): Int {
        return when (difficultLevel) {
            DifficultLevel.EASY -> GAME_DIFFICULT_EASY
            DifficultLevel.MEDIUM -> GAME_DIFFICULT_MEDIUM
            DifficultLevel.HARD -> GAME_DIFFICULT_HARD
            DifficultLevel.EXPERT -> GAME_DIFFICULT_HARD
        }
    }

    fun getLivesCount(difficultLevel: DifficultLevel): Int {
        return when (difficultLevel) {
            DifficultLevel.EASY -> LIVES_EASY
            DifficultLevel.MEDIUM -> LIVES_MEDIUM
            DifficultLevel.HARD -> LIVES_HARD
            DifficultLevel.EXPERT -> LIVES_EXPERT
        }
    }

    fun Context.stringByName(name: String, uiLanguage: Language): String {
        val localized = withLanguage(uiLanguage)
        val resId = localized.resources.getIdentifier(
            name, "string", localized.packageName
        )

        return if (resId != 0) {
            localized.getString(resId)
        } else {
            name
        }
    }

    fun Context.drawableIdByName(name: String): Int {
        val id = resources.getIdentifier(name, "drawable", packageName)

        return if (id != 0) {
            id
        } else {
            R.drawable.ic_category_miscellaneous
        }
    }


    fun createCategoryChip(parent: ViewGroup, item: CategoryUi): Chip {
        val ctx = parent.context
        val chip =
            LayoutInflater.from(ctx).inflate(R.layout.view_category_chip, parent, false) as Chip

        chip.text = item.title
        chip.isCheckable = true
        chip.tag = item.key
        chip.chipBackgroundColor = ContextCompat.getColorStateList(
            ctx, R.color.selector_options_button_bg
        )
        if (item.iconRes != 0) {
            chip.chipIcon = AppCompatResources.getDrawable(ctx, item.iconRes)
            chip.isChipIconVisible = true
        } else {
            chip.isChipIconVisible = false
        }

        return chip
    }

    fun Context.withLanguage(lang: Language): Context {
        val locale = when (lang) {
            Language.RUSSIAN -> Locale("ru")
            Language.SPANISH -> Locale("es")
            Language.ENGLISH -> Locale("en")
            Language.FRENCH -> Locale("fr")
            Language.GERMAN -> Locale("de")
        }

        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        return createConfigurationContext(config)
    }

    fun colorWithAlpha(color: Int, alpha: Float): Int {
        val a = (alpha.coerceIn(
            ConstantsApp.EMPTY_ALPHA, ConstantsApp.FULL_ALPHA
        ) * ConstantsApp.COLOR_MAX_CHANNEL).toInt()

        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)

        return Color.argb(a, r, g, b)
    }
}
