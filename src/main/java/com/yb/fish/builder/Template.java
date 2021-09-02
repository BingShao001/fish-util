package com.yb.fish.builder;

public class Template {

    private final String firstName;

    private final String lastName;

    Template(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        String firstName;
        String lastName;

        Builder firstName(String value) {
            this.firstName = value;
            return this;
        }

        Builder lastName(String value) {
            this.lastName = value;
            return this;
        }

        public Template build() {
            /**
             * 校验熟悉的逻辑
             */
            return new Template(this);
        }
    }
}

