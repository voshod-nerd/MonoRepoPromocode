package com.voshodnerd

import java.io.File

public class PromoCodeCreator {

    fun main() {
        // Параметры генерации промокодов
        val numberOfPromocodes = 4 * 100000  // Количество промокодов
        val promocodeLength = 8 // Длина каждого промокода
        val outputFile = "promocodes$numberOfPromocodes.csv" // Имя выходного файла

        // Генерация промокодов
        val promocodes = generatePromocodes(numberOfPromocodes, promocodeLength)

        // Запись промокодов в CSV файл
        writePromocodesToCSV(promocodes, outputFile)

        println("Промокоды успешно сгенерированы и записаны в файл: $outputFile")
    }

    // Функция для генерации списка промокодов
    fun generatePromocodes(count: Int, length: Int): List<String> {
        val allowedChars = ('A'..'Z') + ('0'..'9') // Разрешенные символы (латинские буквы и цифры)
        return List(count) {
            (1..length).map { allowedChars.random() }.joinToString("")
        }
    }

    // Функция для записи промокодов в CSV файл
    fun writePromocodesToCSV(promocodes: List<String>, fileName: String) {
        File(fileName).bufferedWriter().use { writer ->
            // Записываем каждый промокод в новой строке
            promocodes.forEach { writer.write("$it\n") }
        }
    }
}