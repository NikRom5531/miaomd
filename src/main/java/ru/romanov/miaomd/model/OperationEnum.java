package ru.romanov.miaomd.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationEnum {
    NEGATIVE("Негатив"),
    POWER_LAW_TRANSFORMATION("Степенное преобразование"),
    CUT_BRIGHTNESS_RANGE("Вырезание диапазона яркостей"),
    LINEAR_SMOOTHING_FILTER("Линейный сглаживающий (усредняющий) фильтр"),
    MEDIAN_FILTER("Медианный фильтр"),
    ROBERTS_GRADIENT("Градиент Робертса"),
    SOBEL_GRADIENT("Градиент Собеля"),
    LAPLACIAN("Лапласиан"),
    IMAGE_HISTOGRAM("Гистограмма изображения"),
    EQUALIZATION_HISTOGRAM("Эквализация гистограммы"),
    GLOBAL_THRESHOLD_FILTER("Пороговый фильтр с глобальным порогом"),
    OTSU_METHOD_THRESHOLD_FILTER("Пороговый фильтр методом Оцу"),
    DILATION_MORPHOLOGY("Морфология: дилатация"),
    EROSION_MORPHOLOGY("Морфология: эрозия"),
    CLOSURE_MORPHOLOGY("Морфология: замыкание"),
    OPENING_MORPHOLOGY("Морфология: размыкание"),
    SELECTION_BOUNDARIES_MORPHOLOGY("Морфология: выделение границ"),
    SKELETON_MORPHOLOGY("Морфология: остов"),
    GAUSSIAN_BLUR("Гауссов фильтр");

    private final String string;
}
