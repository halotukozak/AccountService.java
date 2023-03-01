class MethodFinder {

    public static String findMethod(String methodName, String[] classNames) {
        for (var className : classNames) {
            try {
                Class<?> c = Class.forName(className);
                for (var method : c.getMethods()) if (method.getName().equals(methodName)) return c.getName();
            } catch (Exception ignored) {
            }
        }
        return "";
    }
}