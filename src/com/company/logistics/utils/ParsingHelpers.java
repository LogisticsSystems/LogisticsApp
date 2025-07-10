package com.company.logistics.utils;

import com.company.logistics.exceptions.InvalidUserInputException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ParsingHelpers {

    public static int tryParseInt(String valueToParse, String parameterName) {
        try{
            return Integer.parseInt(valueToParse);
        } catch (NumberFormatException e) {
            throw new InvalidUserInputException(String.format(ErrorMessages.INCORRECT_DATA_INPUT
                    ,parameterName,"number"));
        }

    }

    public static double tryParseDouble(String valueToParse, String parameterName) {
        try{
            return Double.parseDouble(valueToParse);
        } catch (NumberFormatException e) {
            throw new InvalidUserInputException(String.format(ErrorMessages.INCORRECT_DATA_INPUT
                    ,parameterName,"fractional number"));
        }
    }

    public static <E extends Enum<E>> E tryParseEnum(String valueToParse, Class<E> type, String errorMessage) {
        try{
            return Enum.valueOf(type,valueToParse.toUpperCase());
        } catch (Exception e) {
            String enumName=type.getSimpleName();
            E[] enumValues=type.getEnumConstants();
            String options= Arrays.stream(enumValues)
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            throw new InvalidUserInputException(String.format(ErrorMessages.INVALID_ENUM_VALUE
                    ,enumName
                    ,options));
        }
    }

    public static LocalDateTime tryParseDateTime(String valueToParse) {
        try{
            return LocalDateTime.parse(valueToParse);
        }catch (Exception e){
            throw new InvalidUserInputException(String.format(ErrorMessages.INCORRECT_DATE_TIME_INPUT));
        }
    }

}
