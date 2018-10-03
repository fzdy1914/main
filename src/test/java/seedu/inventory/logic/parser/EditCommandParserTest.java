package seedu.inventory.logic.parser;

import static seedu.inventory.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.inventory.logic.commands.CommandTestUtil.IMAGE_DESC_OPPO;
import static seedu.inventory.logic.commands.CommandTestUtil.IMAGE_DESC_SONY;
import static seedu.inventory.logic.commands.CommandTestUtil.INVALID_IMAGE_DESC;
import static seedu.inventory.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.inventory.logic.commands.CommandTestUtil.INVALID_QUANTITY_DESC;
import static seedu.inventory.logic.commands.CommandTestUtil.INVALID_SKU_DESC;
import static seedu.inventory.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.inventory.logic.commands.CommandTestUtil.NAME_DESC_OPPO;
import static seedu.inventory.logic.commands.CommandTestUtil.QUANTITY_DESC_OPPO;
import static seedu.inventory.logic.commands.CommandTestUtil.QUANTITY_DESC_SONY;
import static seedu.inventory.logic.commands.CommandTestUtil.SKU_DESC_OPPO;
import static seedu.inventory.logic.commands.CommandTestUtil.SKU_DESC_SONY;
import static seedu.inventory.logic.commands.CommandTestUtil.TAG_DESC_GADGET;
import static seedu.inventory.logic.commands.CommandTestUtil.TAG_DESC_SMARTPHONE;
import static seedu.inventory.logic.commands.CommandTestUtil.VALID_IMAGE_OPPO;
import static seedu.inventory.logic.commands.CommandTestUtil.VALID_IMAGE_SONY;
import static seedu.inventory.logic.commands.CommandTestUtil.VALID_NAME_OPPO;
import static seedu.inventory.logic.commands.CommandTestUtil.VALID_QUANTITY_OPPO;
import static seedu.inventory.logic.commands.CommandTestUtil.VALID_QUANTITY_SONY;
import static seedu.inventory.logic.commands.CommandTestUtil.VALID_SKU_OPPO;
import static seedu.inventory.logic.commands.CommandTestUtil.VALID_SKU_SONY;
import static seedu.inventory.logic.commands.CommandTestUtil.VALID_TAG_GADGET;
import static seedu.inventory.logic.commands.CommandTestUtil.VALID_TAG_SMARTPHONE;
import static seedu.inventory.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.inventory.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.inventory.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.inventory.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static seedu.inventory.testutil.TypicalIndexes.INDEX_SECOND_ITEM;
import static seedu.inventory.testutil.TypicalIndexes.INDEX_THIRD_ITEM;

import org.junit.Test;

import seedu.inventory.commons.core.index.Index;
import seedu.inventory.logic.commands.EditCommand;
import seedu.inventory.logic.commands.EditCommand.EditItemDescriptor;
import seedu.inventory.model.item.Image;
import seedu.inventory.model.item.Name;
import seedu.inventory.model.item.Quantity;
import seedu.inventory.model.item.Sku;
import seedu.inventory.model.tag.Tag;
import seedu.inventory.testutil.EditPersonDescriptorBuilder;

public class EditCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_OPPO, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_OPPO, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_OPPO, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 u/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_NAME_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_QUANTITY_DESC, Quantity.MESSAGE_QUANTITY_CONSTRAINTS); // invalid phone
        assertParseFailure(parser, "1" + INVALID_SKU_DESC, Sku.MESSAGE_SKU_CONSTRAINTS); // invalid email
        assertParseFailure(parser, "1" + INVALID_IMAGE_DESC, Image.MESSAGE_IMAGE_CONSTRAINTS); // invalid inventory
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_TAG_CONSTRAINTS); // invalid tag

        // invalid phone followed by valid email
        assertParseFailure(parser, "1" + INVALID_QUANTITY_DESC + SKU_DESC_OPPO, Quantity.MESSAGE_QUANTITY_CONSTRAINTS);

        // valid phone followed by invalid phone. The test case for invalid phone followed by valid phone
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, "1" + QUANTITY_DESC_SONY + INVALID_QUANTITY_DESC,
                Quantity.MESSAGE_QUANTITY_CONSTRAINTS);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Item} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + TAG_DESC_GADGET + TAG_DESC_SMARTPHONE + TAG_EMPTY,
                Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_DESC_GADGET + TAG_EMPTY + TAG_DESC_SMARTPHONE,
                Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_GADGET + TAG_DESC_SMARTPHONE,
                Tag.MESSAGE_TAG_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + INVALID_SKU_DESC + VALID_IMAGE_OPPO + VALID_QUANTITY_OPPO,
                Name.MESSAGE_NAME_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_ITEM;
        String userInput = targetIndex.getOneBased() + QUANTITY_DESC_SONY + TAG_DESC_SMARTPHONE
                + SKU_DESC_OPPO + IMAGE_DESC_OPPO + NAME_DESC_OPPO + TAG_DESC_GADGET;

        EditCommand.EditItemDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_OPPO)
                .withPhone(VALID_QUANTITY_SONY).withEmail(VALID_SKU_OPPO).withAddress(VALID_IMAGE_OPPO)
                .withTags(VALID_TAG_SMARTPHONE, VALID_TAG_GADGET).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_ITEM;
        String userInput = targetIndex.getOneBased() + QUANTITY_DESC_SONY + SKU_DESC_OPPO;

        EditItemDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_QUANTITY_SONY)
                .withEmail(VALID_SKU_OPPO).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_ITEM;
        String userInput = targetIndex.getOneBased() + NAME_DESC_OPPO;
        EditItemDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_OPPO).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = targetIndex.getOneBased() + QUANTITY_DESC_OPPO;
        descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_QUANTITY_OPPO).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = targetIndex.getOneBased() + SKU_DESC_OPPO;
        descriptor = new EditPersonDescriptorBuilder().withEmail(VALID_SKU_OPPO).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // inventory
        userInput = targetIndex.getOneBased() + IMAGE_DESC_OPPO;
        descriptor = new EditPersonDescriptorBuilder().withAddress(VALID_IMAGE_OPPO).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_GADGET;
        descriptor = new EditPersonDescriptorBuilder().withTags(VALID_TAG_GADGET).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_ITEM;
        String userInput = targetIndex.getOneBased() + QUANTITY_DESC_OPPO + IMAGE_DESC_OPPO + SKU_DESC_OPPO
                + TAG_DESC_GADGET + QUANTITY_DESC_OPPO + IMAGE_DESC_OPPO + SKU_DESC_OPPO + TAG_DESC_GADGET
                + QUANTITY_DESC_SONY + IMAGE_DESC_SONY + SKU_DESC_SONY + TAG_DESC_SMARTPHONE;

        EditItemDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_QUANTITY_SONY)
                .withEmail(VALID_SKU_SONY).withAddress(VALID_IMAGE_SONY).withTags(VALID_TAG_GADGET,
                        VALID_TAG_SMARTPHONE)
                .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_ITEM;
        String userInput = targetIndex.getOneBased() + INVALID_QUANTITY_DESC + QUANTITY_DESC_SONY;
        EditItemDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_QUANTITY_SONY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + SKU_DESC_SONY + INVALID_QUANTITY_DESC + IMAGE_DESC_SONY
                + QUANTITY_DESC_SONY;
        descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_QUANTITY_SONY).withEmail(VALID_SKU_SONY)
                .withAddress(VALID_IMAGE_SONY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_ITEM;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditItemDescriptor descriptor = new EditPersonDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
