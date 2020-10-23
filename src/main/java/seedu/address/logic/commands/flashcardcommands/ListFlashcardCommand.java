package seedu.address.logic.commands.flashcardcommands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_FLASHCARDS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.FlashcardModel;
import seedu.address.model.flashcard.Flashcard;
import seedu.address.model.flashcard.FlashcardSet;


/**
 * Regulates the behaviour of a {@code Command} that lists down
 * {@code Flashcard}s of a {@code FlashcardSet}
 */
public class ListFlashcardCommand extends Command<FlashcardModel> {

    public static final String COMMAND_WORD = "list fl";
    public static final String MESSAGE_SUCCESS = "Listed all flashcard in the selected flashcard set";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists flashcards in a flashcard set."
            + "Parameters: "
            + "<flashcardsetindex> "
            + "Example: " + COMMAND_WORD + " 1";

    private final Index flashcardSetIndex;

    /**
     * Creates a {@code Command} to list all {@code Flashcard}s
     * of a {@code FlashcardSet} with a specified {@code Index}
     */
    public ListFlashcardCommand(Index flashcardSetIndex) {
        this.flashcardSetIndex = flashcardSetIndex;
    }

    @Override
    public CommandResult execute(FlashcardModel model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredFlashcardSetList(PREDICATE_SHOW_ALL_FLASHCARDS);

        FlashcardSet flashcardSet = model.getFlashcardSet(flashcardSetIndex);
        List<Flashcard> flashcards = flashcardSet.getFlashcards();
        model.setFlashcardSetToView(flashcardSet);

        return new CommandResult(String.format(
                MESSAGE_SUCCESS + "\n" + "There are %d flashcards",
                flashcards.size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ListFlashcardCommand // instanceof handles nulls
                && flashcardSetIndex.equals(((ListFlashcardCommand) other).flashcardSetIndex)); // state check
    }
}
