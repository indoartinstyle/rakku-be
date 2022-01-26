package in.as.sixtynine.rakku.userservice.utils;

public enum ERole {

    USER {
        @Override
        public String getRoleName() {
            return "ROLE_" + this.name();
        }
    },
    ADMIN {
        @Override
        public String getRoleName() {
            return "ROLE_" + this.name();
        }
    };

    public abstract String getRoleName();
}
