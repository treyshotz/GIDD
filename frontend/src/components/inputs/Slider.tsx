import { withStyles, makeStyles } from '@material-ui/core/styles';
import Slider from '@material-ui/core/Slider';
import Input from '@material-ui/core/Input';
import { useState } from 'react';
import { Control, Controller, RegisterOptions } from 'react-hook-form';

const useStyles = makeStyles((theme) => ({
  root: {
    width: '100%',
  },
  margin: {
    height: theme.spacing(3),
  },
  input: {
    width: 42,
  },
  sliderGrid: {
    display: 'grid',
    gridTemplateColumns: '1fr auto',
    gap: theme.spacing(3),
    marginBottom: theme.spacing(1),
  },
}));

const PrettoSlider = withStyles({
  root: {
    color: '#52af77',
    height: 8,
  },
  thumb: {
    height: 24,
    width: 24,
    backgroundColor: '#fff',
    border: '2px solid currentColor',
    marginTop: -8,
    marginLeft: -12,
    '&:focus, &:hover, &$active': {
      boxShadow: 'inherit',
    },
  },
  active: {},
  valueLabel: {
    left: 'calc(-50% + 4px)',
  },
  track: {
    height: 8,
    borderRadius: 4,
  },
  rail: {
    height: 8,
    borderRadius: 4,
  },
})(Slider);

export type IProps = {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  control: Control<any>;
  name: string;
  rules?: RegisterOptions;
  defaultValue?: number;
};

export default function CustomizedSlider({ name, control, rules = {}, defaultValue = 3 }: IProps) {
  const classes = useStyles();
  const [value, setValue] = useState<number | string | Array<number | string>>(3);

  const handleBlur = () => {
    if (value < 0) {
      setValue(0);
    } else if (value > 15) {
      setValue(15);
    }
  };

  return (
    <div className={classes.root}>
      <div className={classes.sliderGrid}>
        <Controller
          control={control}
          defaultValue={defaultValue}
          name={name}
          render={({ field }) => (
            <>
              <PrettoSlider {...field} aria-labelledby='input-slider' max={15} valueLabelDisplay='auto' />
              <Input
                {...field}
                className={classes.input}
                inputProps={{
                  step: 1,
                  min: 0,
                  max: 15,
                  type: 'number',
                  'aria-labelledby': 'input-slider',
                }}
                margin='dense'
                onBlur={handleBlur}
              />
            </>
          )}
          rules={rules}
        />
      </div>
    </div>
  );
}
