package org.module.dynamic.logger.internal.metadata;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;

public class LoggerOutputTypeResolver implements OutputTypeResolver {

    @Override
    public MetadataType getOutputType(MetadataContext metadataContext, Object o) throws MetadataResolvingException, ConnectionException {
        return null;
    }


    @Override
    public String getCategoryName() {
        return null;
    }
}
