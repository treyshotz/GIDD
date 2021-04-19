import { useForm } from 'react-hook-form';
import { useSnackbar } from 'hooks/Snackbar';
import { useUpdateUser } from 'hooks/User';
import { User } from 'types/Types';
import { TrainingLevel } from 'types/Enums';
import { traningLevelToText } from 'utils';
import { parseISO } from 'date-fns';

// Material UI
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import MenuItem from '@material-ui/core/MenuItem';

// Project components
import DatePicker from 'components/inputs/DatePicker';
import Select from 'components/inputs/Select';
import TextField from 'components/inputs/TextField';
import SubmitButton from 'components/inputs/SubmitButton';

const useStyles = makeStyles((theme) => ({
  list: {
    display: 'grid',
    gap: theme.spacing(1),
  },
}));

export type EditProfileProps = {
  user: User;
};

type UserEditData = Pick<User, 'firstName' | 'surname' | 'email' | 'traninglevel'> & {
  birthDate: Date | null;
};

const EditProfile = ({ user }: EditProfileProps) => {
  const classes = useStyles();
  const updateUser = useUpdateUser();
  const showSnackbar = useSnackbar();
  const { register, control, formState, handleSubmit } = useForm<UserEditData>({
    defaultValues: {
      firstName: user.firstName,
      surname: user.surname,
      email: user.email,
      traninglevel: user.traninglevel,
      birthDate: user.birthDate ? parseISO(user.birthDate.substring(0, 16)) : null,
    },
  });
  const submit = async (data: UserEditData) => {
    updateUser.mutate(
      { userId: user.id, user: { ...data, birthDate: data.birthDate ? data.birthDate.toISOString().substring(0, 16) : null } },
      {
        onSuccess: () => {
          showSnackbar('Profilen ble oppdatert', 'success');
        },
        onError: (e) => {
          showSnackbar(e.detail, 'error');
        },
      },
    );
  };

  return (
    <form className={classes.list} onSubmit={handleSubmit(submit)}>
      <Typography variant='h3'>Oppdater profil</Typography>
      <TextField disabled={updateUser.isLoading} formState={formState} label='Fornavn' {...register('firstName', { required: 'Feltet er påkrevd' })} required />
      <TextField disabled={updateUser.isLoading} formState={formState} label='Etternavn' {...register('surname', { required: 'Feltet er påkrevd' })} required />
      <TextField
        disabled={updateUser.isLoading}
        formState={formState}
        label='Epost'
        {...register('email', { required: 'Feltet er påkrevd' })}
        required
        type='email'
      />
      <DatePicker control={control} dateProps={{ disableFuture: true }} formState={formState} label='Fødselsdato' name='birthDate' type='date' />
      <Select control={control} formState={formState} label='Trenings-nivå' name='traninglevel'>
        {Object.keys(TrainingLevel).map((value, index) => (
          <MenuItem key={index} value={value}>
            {traningLevelToText(value as TrainingLevel)}
          </MenuItem>
        ))}
      </Select>
      <SubmitButton disabled={updateUser.isLoading} formState={formState}>
        Oppdater bruker
      </SubmitButton>
    </form>
  );
};

export default EditProfile;
