package com.smarcom.smarpay.cloudapi.model;

import com.google.api.client.util.Key;

public class ClientAction {

    @Key("State")
    private StateInfo state;

    public StateInfo getState() {
        return state;
    }

    public void setState(StateInfo state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientAction)) return false;

        ClientAction that = (ClientAction) o;

        if (!state.equals(that.state)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    @Override
    public String toString() {
        return "StateInfoResult{" +
                "state=" + state +
                '}';
    }
}
