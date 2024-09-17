package com.gabrielanceski.tccifrs.utils;

import com.gabrielanceski.tccifrs.domain.DocumentType;

public class DocumentUtils {
    private static final String NON_NUMBER_REGEX = "[^0-9]";
    private static final int[] CNPJ_FIRST_DIGIT_WEIGHT = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] CNPJ_SECOND_DIGIT_WEIGHT = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    public static boolean hasNonNumbers(String document) {
        return document.matches(NON_NUMBER_REGEX);
    }

    public static String removeNonNumbers(String document) {
        return document.replaceAll(NON_NUMBER_REGEX, "");
    }

    public static DocumentType getDocumentType(String document) {
        if (hasNonNumbers(document.trim())) throw new IllegalArgumentException("Document must contain only numbers");
        return DocumentType.getByLength(document.length());
    }

    public static boolean checkCNPJ(String document) {
        if (document == null || document.isBlank() || getDocumentType(document) != DocumentType.CNPJ) return false;

        // Verifica se todos os dígitos são iguais (CNPJs como "11111111111111" são inválidos)
        if (document.chars().distinct().count() == 1) {
            return false;
        }

        // Cálculo do primeiro dígito verificador
        int firstDigit = cnpjDigit(document.substring(0, 12), CNPJ_FIRST_DIGIT_WEIGHT);

        // Cálculo do segundo dígito verificador
        int secondDigit = cnpjDigit(document.substring(0, 13), CNPJ_SECOND_DIGIT_WEIGHT);

        // Verifica se os dígitos calculados batem com os dígitos fornecidos
        return document.charAt(12) == Character.forDigit(firstDigit, 10) &&
                document.charAt(13) == Character.forDigit(secondDigit, 10);
    }

    private static int cnpjDigit(String str, int[] weight) {
        int sum = 0;
        for (int i = 0; i < str.length(); i++) {
            sum += Character.getNumericValue(str.charAt(i)) * weight[i];
        }
        int rest = sum % 11;
        return (rest < 2) ? 0 : 11 - rest;
    }
}
