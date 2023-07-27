import dayjs from 'dayjs';
import { IFilledForm } from 'app/shared/model/filled-form.model';

export interface IFeedbackhistory {
  id?: number;
  feedbackText?: string | null;
  feedbackDate?: string | null;
  filledForm?: IFilledForm | null;
}

export const defaultValue: Readonly<IFeedbackhistory> = {};
