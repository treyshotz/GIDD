import { Link } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import URLS from 'URLS';
import { useSnackbar } from 'hooks/Snackbar';
import { useUpdateUser } from 'hooks/User';
import { User } from 'types/Types';

// Material UI
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';

// Project components
import TextField from 'components/inputs/TextField';
import SubmitButton from 'components/inputs/SubmitButton';
import Container from 'components/layout/Container';

const useStyles = makeStyles((theme) => ({
  list: {
    padding: theme.spacing(2, 0),
    display: 'grid',
    gap: theme.spacing(1),
  },
}));

export type EditProfileProps = {
  user: User;
};

type UserEditData = Pick<User, 'first_name' | 'surname' | 'email'>;

const EditProfile = ({ user }: EditProfileProps) => {
  const classes = useStyles();
  const updateUser = useUpdateUser();
  const showSnackbar = useSnackbar();
  const { register, formState, handleSubmit } = useForm<UserEditData>({
    defaultValues: { first_name: user.first_name, surname: user.surname, email: user.email },
  });
  const submit = async (data: UserEditData) => {
    updateUser.mutate(
      { userId: user.user_id, user: data },
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
    <Container maxWidth='md'>
      <form className={classes.list} onSubmit={handleSubmit(submit)}>
        <Typography variant='h3'>Oppdater profil</Typography>
        <TextField
          disabled={updateUser.isLoading}
          formState={formState}
          label='Fornavn'
          {...register('first_name', { required: 'Feltet er påkrevd' })}
          required
        />
        <TextField
          disabled={updateUser.isLoading}
          formState={formState}
          label='Etternavn'
          {...register('surname', { required: 'Feltet er påkrevd' })}
          required
        />
        <TextField
          disabled={updateUser.isLoading}
          formState={formState}
          label='Epost'
          {...register('email', { required: 'Feltet er påkrevd' })}
          required
          type='email'
        />
        <SubmitButton disabled={updateUser.isLoading} formState={formState}>
          Oppdater bruker
        </SubmitButton>
        <Button component={Link} to={URLS.PROFILE} variant='outlined'>
          Tilbake
        </Button>
      </form>
    </Container>
  );
};

export default EditProfile;
