// Instead of just binding properties, add listeners to activate the right graph
benchBean.getRefMeterDN02().getFlowRateProperty().addListener((obs, oldVal, newVal) -> {
    if (newVal != null && !newVal.equals(oldVal)) {
        graphRefFlowRateDn02.activate();
    }
});

benchBean.getRefMeterDN08().getFlowRateProperty().addListener((obs, oldVal, newVal) -> {
    if (newVal != null && !newVal.equals(oldVal)) {
        graphRefFlowRateDn08.activate();
    }
});

benchBean.getRefMeterDN32().getFlowRateProperty().addListener((obs, oldVal, newVal) -> {
    if (newVal != null && !newVal.equals(oldVal)) {
        graphRefFlowRateDn32.activate();
    }
}); 