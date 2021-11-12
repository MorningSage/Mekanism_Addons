package morningsage.mekanismaddons.utils;

import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class EnumBuilder<T extends Enum<?>> {
    private static final ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
    private final Class<T> enumClass;

    private EnumBuilder(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    public static <T extends Enum<?>> EnumBuilder<T> of(Class<T> enumClass) {
        return new EnumBuilder<>(enumClass);
    }

    public EnumValueBuilder create(String constantName) {
        return new EnumValueBuilder(constantName);
    }

    public class EnumValueBuilder {
        private final String constantName;

        private EnumValueBuilder(String constantName) {
            this.constantName = constantName;
        }

        public T init(Object... args) {
            // 0. Sanity checks
            if (!Enum.class.isAssignableFrom(enumClass)) {
                throw new RuntimeException("class " + enumClass + " is not an instance of Enum");
            }

            // 1. Lookup "$VALUES" holder or the array return type in enum class and get previous enum instances
            HashSet<Field> valuesFields = new HashSet<>();
            Field[] fields = enumClass.getDeclaredFields();

            for (Field field : fields) {
                if (field.getName().contains("$VALUES") || field.getType().getName().equals("[L" + enumClass.getName() + ";")) {
                    valuesFields.add(field);
                }
            }

            if (valuesFields.isEmpty()) return null;

            try {
                // 3. build new enum
                T newValue = makeEnum(
                    constantName, // THE NEW ENUM INSTANCE TO BE DYNAMICALLY ADDED
                    enumClass.getEnumConstants().length,
                    args); // can be used to pass values to the enum constructor

                for (Field valuesField : valuesFields) {
                    AccessibleObject.setAccessible(new Field[]{valuesField}, true);

                    // 2. Copy it
                    T[] previousValues = (T[]) valuesField.get(enumClass);
                    List<T> values = new ArrayList<T>(Arrays.asList(previousValues));

                    // 4. add new value
                    values.add(newValue);

                    // 5. Set new values field
                    setFailsafeFieldValue(valuesField, null, values.toArray((T[]) Array.newInstance(enumClass, 0)));
                }

                // 6. Clean enum cache
                cleanEnumCache(enumClass);

                return newValue;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        private T makeEnum(String value, int ordinal, Object[] additionalValues) {
            Object[] parms = new Object[additionalValues.length + 2];
            parms[0] = value; parms[1] = ordinal;
            System.arraycopy(additionalValues, 0, parms, 2, additionalValues.length);
            return enumClass.cast(getConstructorAccessor(enumClass, parms));
        }
        private Object getConstructorAccessor(Class<?> enumClass, Object[] params) {
            for (Constructor<?> declaredConstructor : enumClass.getDeclaredConstructors()) {
                if (declaredConstructor.getParameterCount() != params.length) continue;

                Class<?>[] paramClazzes = declaredConstructor.getParameterTypes();
                if (paramClazzes[0] != String.class || paramClazzes[1] != int.class) continue;

                try {
                    return reflectionFactory.newConstructorAccessor(declaredConstructor).newInstance(params);
                } catch (Exception exception) {
                    // No Op
                }
            }

            throw new RuntimeException("Failed to find valid constructor");
        }

        private void setFailsafeFieldValue(Field field, Object target, Object value) throws NoSuchFieldException, IllegalAccessException {
            // let's make the field accessible
            field.setAccessible(true);

            // next we change the modifier in the Field instance to
            // not be final anymore, thus tricking reflection into
            // letting us modify the static final field
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            int modifiers = modifiersField.getInt(field);

            // blank out the final bit in the modifiers int
            modifiers &= ~Modifier.FINAL;
            modifiersField.setInt(field, modifiers);

            FieldAccessor fa = reflectionFactory.newFieldAccessor(field, false);
            fa.set(target, value);
        }
        private void cleanEnumCache(Class<?> enumClass) throws NoSuchFieldException, IllegalAccessException {
            blankField(enumClass, "enumConstantDirectory"); // Sun (Oracle?!?) JDK 1.5/6
            blankField(enumClass, "enumConstants"); // IBM JDK
        }
        private void blankField(Class<?> enumClass, String fieldName) throws NoSuchFieldException, IllegalAccessException {
            for (Field field : Class.class.getDeclaredFields()) {
                if (field.getName().contains(fieldName)) {
                    AccessibleObject.setAccessible(new Field[] { field }, true);
                    setFailsafeFieldValue(field, enumClass, null);
                    break;
                }
            }
        }
    }
}
