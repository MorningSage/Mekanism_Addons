package morningsage.mekanismaddons.items;

import appeng.api.definitions.IDefinitions;
import appeng.api.parts.IPart;
import appeng.api.util.AEColor;
import appeng.bootstrap.components.ItemColorComponent;
import appeng.client.render.StaticItemColor;
import appeng.core.Api;
import appeng.core.ApiDefinitions;
import appeng.core.features.registries.PartModels;
import appeng.items.materials.MaterialItem;
import appeng.items.materials.MaterialType;
import appeng.items.parts.PartItem;
import appeng.items.parts.PartModelsHelper;
import appeng.util.Platform;
import com.google.common.base.Preconditions;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import morningsage.mekanismaddons.ae2.AddonMaterialType;
import morningsage.mekanismaddons.ae2.GasStorageCell;
import morningsage.mekanismaddons.ae2.channels.GasStorageChannel;
import morningsage.mekanismaddons.ae2.gas.GasDummyItem;
import morningsage.mekanismaddons.ae2.gas.GasDummyItemColor;
import morningsage.mekanismaddons.ae2.parts.GasTerminalPart;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.Function;

public class AddonAE2Items {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister("mekanismaddons");

    private static PartModels partModels;

    private static final ItemRegistryObject<PartItem<GasTerminalPart>> gasTerminal;
    private static final ItemRegistryObject<GasDummyItem> gasDummyItem;

    static {
        for (MaterialType type : AddonMaterialType.DATA_BY_TYPE.keySet()) {
            Preconditions.checkState(!type.isRegistered(), "Cannot create the same material twice.");

            ITEMS.register(type.getId(), (props) -> {
                MaterialItem item = new MaterialItem(props, type);
                type.setItemInstance(item);
                return item;
            });
            type.markReady();

            ITEMS.register(type.getId().replace("cell_component", "storage_cell"), (properties) ->
                new GasStorageCell(properties, type, AddonMaterialType.DATA_BY_TYPE.get(type).getA() >> 3)
            );
        }

        GasStorageChannel.register(appeng.core.Api.instance().storage());

        gasTerminal = createPart("gas_terminal", GasTerminalPart.class, GasTerminalPart::new, new StaticItemColor(AEColor.TRANSPARENT));
        gasDummyItem = createItem("dummy_gas_item", GasDummyItem::new, new GasDummyItemColor());
    }

    private static <T extends IPart> ItemRegistryObject<PartItem<T>> createPart(String id, Class<T> partClass, Function<ItemStack, T> factory, IItemColor color) {
        Api.instance().registries().partModels().registerModels(PartModelsHelper.createModels(partClass));

        return ITEMS.register(id, (props) -> {
            PartItem<T> item = new PartItem<>(props, factory);
            IDefinitions definitions = Api.instance().definitions();

            if (Platform.hasClientClasses() && definitions instanceof ApiDefinitions) {
                ((ApiDefinitions) definitions).getRegistry().addBootstrapComponent(new ItemColorComponent(item, color));
            }

            return item;
        });
    }

    private static <T extends Item> ItemRegistryObject<T> createItem(String name, Function<Item.Properties, T> factory, IItemColor color) {
        return ITEMS.register(name, properties -> {
            T item = factory.apply(properties);
            IDefinitions definitions = Api.instance().definitions();

            if (Platform.hasClientClasses() && definitions instanceof ApiDefinitions) {
                ((ApiDefinitions) definitions).getRegistry().addBootstrapComponent(new ItemColorComponent(item, color));
            }

            return item;
        });
    }
}
