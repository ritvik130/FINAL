package com.example.servicenovigrad;

import android.content.pm.LabeledIntent;

import com.example.servicenovigrad.data.Class.NovService;

import java.util.List;

public interface ServiceProvider {
    void servicesProvided(List<String> servicesId);
}
