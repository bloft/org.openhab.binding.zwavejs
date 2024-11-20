package org.openhab.binding.zwavejs.model.command;

import org.openhab.binding.zwavejs.model.Command;

public class SetApiSchema extends Command {
    private int schemaVersion;

    public SetApiSchema(int schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public int getSchemaVersion() {
        return schemaVersion;
    }
}
