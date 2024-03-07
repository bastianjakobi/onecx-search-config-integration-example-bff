package org.tkit.onecx.search.config.integration.bff.rs.mappers;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "onecx.search-config-integration-example-bff")
public interface OnecxSearchConfigIntegrationExampleBFFConfig {
    @WithName("ui-app-id")
    String uiAppID();

    @WithName("product-name")
    @WithDefault("${onecx.permissions.product-name}")
    String productName();

    SearchConfig searchconfig();

    interface SearchConfig {
        @WithName("enabled")
        boolean searchConfigEnabled();
    }
}