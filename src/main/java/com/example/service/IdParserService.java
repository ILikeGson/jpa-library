package com.example.service;

import org.springframework.stereotype.Service;

@Service
public class IdParserService implements LongParser{
    @Override
    public Long parse(String id) {
        return Long.parseLong(id.trim());
    }
}
