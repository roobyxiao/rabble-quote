package com.whzz.config;

import com.whzz.applicationsrevice.quote.dto.*;
import com.whzz.domain.bo.*;
import com.whzz.utils.DataTimeUtil;
import com.whzz.utils.SymbolUtil;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Configuration
public class ModelMapperConfiguration {
    //创建自定义转换规则
    Converter<Boolean, String> yesOrNo = new AbstractConverter<Boolean, String>() {
        protected String convert(Boolean source) {
            return source ? "是" : "否";
        }
    };
    Converter<Long, Long> sbVolumeConverter = new AbstractConverter<Long, Long>() {
        @Override
        protected Long convert(Long volume) {
            return volume/100;
        }
    };

    private Converter<SbStockDto, Stock> sbStockDtoStockConverter = new AbstractConverter<SbStockDto, Stock>() {
        @Override
        protected Stock convert(SbStockDto sbStockDto) {
            return Stock.builder().code(SymbolUtil.sbSymbolToCode(sbStockDto.getSymbol()))
                    .name(sbStockDto.getName())
                    .ipoDate(Instant.ofEpochMilli(sbStockDto.getListDate()).atZone(ZoneId.systemDefault()).toLocalDate())
                    .active(true).build();
        }
    };

    private Converter<EmStockDto, Stock> emStockDtoStockConverter = new AbstractConverter<EmStockDto, Stock>() {
        @Override
        protected Stock convert(EmStockDto emStockDto) {
            return Stock.builder().code(SymbolUtil.symbolToCode(emStockDto.getSECURITYCODE()))
                    .name(emStockDto.getSECURITYNAME())
                    .ipoDate(LocalDate.parse(emStockDto.getLISTINGDATE().split(" ")[0]))
                    .active(true).build();
        }
    };

    private Converter<TsLimitDto, Daily> tsLimitDtoDailyConverter = new AbstractConverter<TsLimitDto, Daily>() {
        @Override
        protected Daily convert(TsLimitDto tsLimitDto) {
            return Daily.builder().code(SymbolUtil.tsCodeToCode(tsLimitDto.getTsCode()))
                    .date(LocalDate.parse(tsLimitDto.getTradeDate(), DateTimeFormatter.BASIC_ISO_DATE))
                    .limitUp(tsLimitDto.getUpLimit()).limitDown(tsLimitDto.getDownLimit()).build();
        }
    };

    private Converter<EmLimitDto, UpLimit> upLimitDtoUpLimitConverter = new AbstractConverter<EmLimitDto, UpLimit>() {
        @Override
        protected UpLimit convert(EmLimitDto emLimitDto) {
            return UpLimit.builder().code(SymbolUtil.symbolToCode(emLimitDto.getC()))
                    .firstTime(DataTimeUtil.emTimeToLocalTime(emLimitDto.getFbt()))
                    .endTime(DataTimeUtil.emTimeToLocalTime(emLimitDto.getLbt()))
                    .open(emLimitDto.getZbc())
                    .last(emLimitDto.getLbc()).build();
        }
    };

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // 官方配置说明： http://modelmapper.org/user-manual/configuration/
        // 完全匹配
        modelMapper.getConfiguration().setFullTypeMatchingRequired(true);

        // 匹配策略使用严格模式
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.addConverter(sbStockDtoStockConverter);
        modelMapper.addConverter(emStockDtoStockConverter);
        modelMapper.addConverter(tsLimitDtoDailyConverter);
        modelMapper.addConverter(upLimitDtoUpLimitConverter);
        //modelMapper.addConverter(backDtoLimitUpExportDtoConverter);
        configure(modelMapper);

        return modelMapper;
    }

    private void configure(ModelMapper modelMapper) {
        /*PropertyMap<BackDto,LimitUpExportDto> propertyMap = new PropertyMap<BackDto, LimitUpExportDto>() {
            @Override
            protected void configure() {
                using(yesOrNo).map(source.isLimitUp(),destination.getLimitUp());//使用自定义转换规则
                using(yesOrNo).map(source.isLimitKeep(), destination.getLimitKeep());
            }
        };
        modelMapper.typeMap(BackDto.class, LimitUpExportDto.class)
                .addMappings(propertyMap);*/
        PropertyMap<SbDailyDto, Daily> dailyPropertyMap = new PropertyMap<SbDailyDto, Daily>() {
            @Override
            protected void configure() {
                using(sbVolumeConverter).map(source.getVolume(), destination.getVolume());
            }
        };
        PropertyMap<SbDailyDto, Forward> forwardPropertyMap = new PropertyMap<SbDailyDto, Forward>() {
            @Override
            protected void configure() {
                using(sbVolumeConverter).map(source.getVolume(), destination.getVolume());
            }
        };
        modelMapper.typeMap(SbDailyDto.class, Daily.class)
                .addMappings(dailyPropertyMap);
        modelMapper.typeMap(SbDailyDto.class, Forward.class)
                .addMappings(forwardPropertyMap);
        modelMapper.typeMap(BsTradeCalDto.class, TradeCal.class)
                .addMappings(mapper -> mapper.map(BsTradeCalDto::getCalendarDate, TradeCal::setDate))
                .addMappings(mapper -> mapper.map(BsTradeCalDto::getIsTradingDay, TradeCal::setOpen));
        modelMapper.typeMap(BsStockDto.class, Stock.class)
                .addMappings(mapper -> mapper.map(BsStockDto::getCodeName, Stock::setName))
                .addMappings(mapper -> mapper.map(BsStockDto::getStatus, Stock::setActive));
        modelMapper.typeMap(EmDailyDto.class, Daily.class)
                .addMappings(mapper -> mapper.map(EmDailyDto::getF46, Daily::setOpen))
                .addMappings(mapper -> mapper.map(EmDailyDto::getF44, Daily::setHigh))
                .addMappings(mapper -> mapper.map(EmDailyDto::getF45, Daily::setLow))
                .addMappings(mapper -> mapper.map(EmDailyDto::getF43, Daily::setClose))
                .addMappings(mapper -> mapper.map(EmDailyDto::getF60, Daily::setLastClose))
                .addMappings(mapper -> mapper.map(EmDailyDto::getF47, Daily::setVolume))
                .addMappings(mapper -> mapper.map(EmDailyDto::getF48, Daily::setAmount))
                .addMappings(mapper -> mapper.map(EmDailyDto::getF57, Daily::setCode))
                .addMappings(mapper -> mapper.map(EmDailyDto::getF168, Daily::setTurn))
                .addMappings(mapper -> mapper.map(EmDailyDto::getF170, Daily::setPercent))
                .addMappings(mapper -> mapper.map(EmDailyDto::getF51, Daily::setLimitUp))
                .addMappings(mapper -> mapper.map(EmDailyDto::getF52, Daily::setLimitDown));
        modelMapper.typeMap(EmDailyDto.class, Forward.class)
                .addMappings(mapper -> mapper.map(EmDailyDto::getF46, Forward::setOpen))
                .addMappings(mapper -> mapper.map(EmDailyDto::getF44, Forward::setHigh))
                .addMappings(mapper -> mapper.map(EmDailyDto::getF45, Forward::setLow))
                .addMappings(mapper -> mapper.map(EmDailyDto::getF43, Forward::setClose))
                .addMappings(mapper -> mapper.map(EmDailyDto::getF60, Forward::setLastClose))
                .addMappings(mapper -> mapper.map(EmDailyDto::getF47, Forward::setVolume))
                .addMappings(mapper -> mapper.map(EmDailyDto::getF48, Forward::setAmount))
                .addMappings(mapper -> mapper.map(EmDailyDto::getF168, Forward::setTurn))
                .addMappings(mapper -> mapper.map(EmDailyDto::getF170, Forward::setPercent));
    }
}
