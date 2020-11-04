package seedu.studybananas.logic.commands.quizcommands;

import static java.util.Objects.requireNonNull;

import seedu.studybananas.commons.core.index.Index;
import seedu.studybananas.logic.commands.Command;
import seedu.studybananas.logic.commands.commandresults.CommandResult;
import seedu.studybananas.logic.commands.commandresults.QuizCommandResult;
import seedu.studybananas.logic.commands.exceptions.CommandException;
import seedu.studybananas.model.FlashcardQuizModel;
import seedu.studybananas.model.flashcard.FlashcardSet;
import seedu.studybananas.model.flashcard.Question;
import seedu.studybananas.model.quiz.Quiz;
import seedu.studybananas.ui.quizui.QuizCard;

public class StartCommand extends Command<FlashcardQuizModel> {

    public static final String COMMAND_WORD = "quiz flset:";
    public static final String MESSAGE_QUIZ_IN_PROGRESS = "A quiz is already in progress! "
            + "Key 'refresh' to see current question/answer. \n"
            + "To stop the current quiz, key 'cancel'.";

    public static final String MESSAGE_FLASHCARD_SET_NONEXISTENT = "Flashcard set does not exist";

    public static final String MESSAGE_FLASHCARD_SET_EMPTY = "Flashcard set is empty";

    private final int index;

    private FlashcardQuizModel model;

    public StartCommand(int index) {
        this.index = index;
    }

    @Override
    public CommandResult execute(FlashcardQuizModel model) throws CommandException {
        requireNonNull(model);

        this.model = model;

        if (model.hasStarted()) {
            throw new CommandException(MESSAGE_QUIZ_IN_PROGRESS);
        }

        try {
            Index indexWrapper = Index.fromOneBased(index);
            FlashcardSet flashcardSet = model.getFlashcardSet(indexWrapper);

            if (flashcardSet.getSize() == 0) { // check for empty flashcard set
                throw new CommandException(MESSAGE_FLASHCARD_SET_EMPTY);
            }

            Quiz quiz = new Quiz(this.index, flashcardSet);
            Question firstQuestion = model.start(quiz);
            QuizCard.setQuestion(firstQuestion);
            QuizCommandUtil.setStatus(Status.ON_QUESTION);

            String feedback = QuizCommandUtil.MESSAGE_AVAIL_ON_QUESTION;
            QuizCommandUtil.updateCommandResult(feedback);

            return new QuizCommandResult(feedback, quiz);

        } catch (IndexOutOfBoundsException e) {
            throw new CommandException(MESSAGE_FLASHCARD_SET_NONEXISTENT);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof StartCommand // instanceof handles nulls
                && index == (((StartCommand) other).index)); // state check
    }

    /**
     * Get the {@Code Index} of the flashcardSet for the quiz.
     */
    public Index getQuizIndex() {
        return Index.fromOneBased(index);
    }
}
